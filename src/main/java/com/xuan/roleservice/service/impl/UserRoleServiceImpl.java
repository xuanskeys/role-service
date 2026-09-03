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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private static final int NOT_DELETED = 0;

    private final RoleMapper roleMapper;

    public UserRoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Long bindUserRole(UserRoleDTO dto) {
        // 同一用户同一角色不重复绑定
        Long existId = baseMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, dto.getUserId())
                .eq(UserRole::getRoleId, dto.getRoleId())
                .eq(UserRole::getIsDelete, NOT_DELETED));
        if (existId != null && existId > 0) {
            throw new IllegalArgumentException("该用户已绑定此角色");
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
        return baseMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId)) > 0;
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        return baseMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)) > 0;
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        return baseMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleId)) > 0;
    }

    @Override
    public boolean deleteByTenantId(Long tenantId) {
        List<Long> roleIds = roleMapper.selectIdsByTenantId(tenantId);
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }
        return baseMapper.delete(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIds)) > 0;
    }
}
