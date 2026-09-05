package com.xuan.roleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.roleservice.entity.dto.UserRoleDTO;
import com.xuan.roleservice.entity.model.UserRole;
import com.xuan.roleservice.mapper.RoleMapper;
import com.xuan.roleservice.mapper.UserRoleMapper;
import com.xuan.roleservice.service.IUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private static final int NOT_DELETED = 0;
    private static final int DELETED = 1;
    private static final long SYSTEM_ADMIN_USER_ID = 0L;
    private static final long SYSTEM_ADMIN_ROLE_ID = 0L;
    private static final long DEFAULT_ROLE_ID = 1L;
    private static final long INTERNAL_TENANT_ID = 0L;
    private static final long DEFAULT_TENANT_ID = 1L;

    private final RoleMapper roleMapper;

    public UserRoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Long bindUserRole(UserRoleDTO dto) {
        var role = roleMapper.selectOne(new LambdaQueryWrapper<com.xuan.roleservice.entity.model.Role>()
                .eq(com.xuan.roleservice.entity.model.Role::getId, dto.getRoleId())
                .eq(com.xuan.roleservice.entity.model.Role::getIsDelete, NOT_DELETED));
        if (role == null) {
            throw new IllegalArgumentException("角色不存在：" + dto.getRoleId());
        }
        if (dto.getRoleId() == SYSTEM_ADMIN_ROLE_ID && dto.getUserId() != SYSTEM_ADMIN_USER_ID) {
            throw new IllegalArgumentException("最高权限角色只允许绑定系统管理员");
        }
        if (dto.getRoleId() == DEFAULT_ROLE_ID && role.getTenantId() != DEFAULT_TENANT_ID) {
            throw new IllegalStateException("默认角色必须关联 tenant_id=1");
        }

        UserRole existing = baseMapper.selectOne(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, dto.getUserId())
                .eq(UserRole::getRoleId, dto.getRoleId()));
        if (existing != null) {
            if (existing.getIsDelete() == NOT_DELETED) {
                throw new IllegalArgumentException("该用户已绑定此角色");
            }
            existing.setDescription(dto.getDescription());
            existing.setIsDelete(NOT_DELETED);
            existing.setDeleteTime(null);
            existing.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(existing);
            return existing.getId();
        }
        UserRole entity = new UserRole();
        BeanUtils.copyProperties(dto, entity);
        entity.setIsDelete(NOT_DELETED);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public List<UserRole> listByUserId(Long userId) {
        return baseMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getIsDelete, NOT_DELETED));
    }

    @Override
    public boolean deleteUserRole(Long userId, Long roleId) {
        assertNotReserved(userId, roleId);
        return logicalDelete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId));
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        assertNotReserved(userId, null);
        return logicalDelete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        if (roleId == DEFAULT_ROLE_ID) {
            throw new IllegalArgumentException("默认角色的全部绑定不允许批量删除");
        }
        assertNotReserved(null, roleId);
        return logicalDelete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
    }

    @Override
    public boolean deleteByTenantId(Long tenantId) {
        if (tenantId == INTERNAL_TENANT_ID) {
            throw new IllegalArgumentException("内部系统租户的角色关联不允许删除");
        }
        List<Long> roleIds = roleMapper.selectIdsByTenantId(tenantId);
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }
        return logicalDelete(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, roleIds));
    }

    private boolean logicalDelete(LambdaQueryWrapper<UserRole> wrapper) {
        List<UserRole> rows = baseMapper.selectList(wrapper.eq(UserRole::getIsDelete, NOT_DELETED));
        if (rows.isEmpty()) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        rows.forEach(row -> {
            row.setIsDelete(DELETED);
            row.setDeleteTime(now);
            row.setUpdateTime(now);
            baseMapper.updateById(row);
        });
        return true;
    }

    private void assertNotReserved(Long userId, Long roleId) {
        if ((userId != null && userId == SYSTEM_ADMIN_USER_ID)
                || (roleId != null && roleId == SYSTEM_ADMIN_ROLE_ID)) {
            throw new IllegalArgumentException("系统管理员与最高权限角色的关联不允许删除");
        }
    }
}
