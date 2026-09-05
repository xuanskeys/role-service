package com.xuan.roleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.roleservice.entity.dto.RoleDTO;
import com.xuan.roleservice.entity.model.Role;
import com.xuan.roleservice.mapper.RoleMapper;
import com.xuan.roleservice.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private static final int NOT_DELETED = 0;
    private static final long SYSTEM_ADMIN_ROLE_ID = 0L;
    private static final long DEFAULT_ROLE_ID = 1L;
    private static final long INTERNAL_TENANT_ID = 0L;

    @Override
    public Role getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Role> listByRoleType(String roleType) {
        return baseMapper.selectList(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleType, roleType)
                .eq(Role::getIsDelete, NOT_DELETED));
    }

    @Override
    public Long createRole(RoleDTO dto) {
        if (dto.getTenantId() == INTERNAL_TENANT_ID) {
            throw new IllegalArgumentException("tenant_id=0 的角色只能由数据库初始化脚本创建");
        }
        // 同一租户下角色名称不允许重复
        if (existsRoleName(dto.getRoleName(), dto.getTenantId(), null)) {
            throw new IllegalArgumentException("角色名称已存在：" + dto.getRoleName());
        }
        Role entity = new Role();
        BeanUtils.copyProperties(dto, entity);
        entity.setRoleType(dto.getRoleType().name());
        entity.setRoleLevel(dto.getRoleLevel() != null ? dto.getRoleLevel() : dto.getRoleType().getRoleLevel());
        entity.setIsDelete(NOT_DELETED);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean updateRole(RoleDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("编辑角色时角色ID不能为空");
        }
        if (dto.getId() == SYSTEM_ADMIN_ROLE_ID || dto.getId() == DEFAULT_ROLE_ID) {
            throw new IllegalArgumentException("系统保留角色不允许修改");
        }
        if (dto.getTenantId() == INTERNAL_TENANT_ID) {
            throw new IllegalArgumentException("tenant_id=0 仅供内部系统使用");
        }
        Role exist = baseMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getId, dto.getId())
                .eq(Role::getIsDelete, NOT_DELETED));
        if (exist == null) {
            throw new IllegalArgumentException("角色不存在：" + dto.getId());
        }
        // 改名时需保证新名称不与同租户其它角色冲突
        if (existsRoleName(dto.getRoleName(), dto.getTenantId(), dto.getId())) {
            throw new IllegalArgumentException("角色名称已存在：" + dto.getRoleName());
        }
        BeanUtils.copyProperties(dto, exist);
        exist.setRoleType(dto.getRoleType().name());
        if (dto.getRoleLevel() == null) {
            exist.setRoleLevel(dto.getRoleType().getRoleLevel());
        }
        exist.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(exist) > 0;
    }

    @Override
    public boolean deleteRole(Long id) {
        if (id == SYSTEM_ADMIN_ROLE_ID || id == DEFAULT_ROLE_ID) {
            throw new IllegalArgumentException("系统保留角色不允许删除");
        }
        Role exist = baseMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getId, id)
                .eq(Role::getIsDelete, NOT_DELETED));
        if (exist == null) {
            return false;
        }
        exist.setIsDelete(1);
        exist.setDeleteTime(LocalDateTime.now());
        exist.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(exist) > 0;
    }

    /**
     * 校验同一租户下角色名称是否已存在（排除指定 id）
     */
    private boolean existsRoleName(String roleName, Long tenantId, Long excludeId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleName, roleName)
                .eq(Role::getIsDelete, NOT_DELETED);
        if (tenantId != null) {
            wrapper.eq(Role::getTenantId, tenantId);
        }
        if (excludeId != null) {
            wrapper.ne(Role::getId, excludeId);
        }
        return baseMapper.selectCount(wrapper) > 0;
    }
}
