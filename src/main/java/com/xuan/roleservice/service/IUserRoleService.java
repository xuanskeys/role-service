package com.xuan.roleservice.service;

import com.xuan.roleservice.entity.dto.UserRoleDTO;
import com.xuan.roleservice.entity.model.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 绑定用户角色（同一用户同一角色不重复绑定）
     *
     * @param dto 绑定入参
     * @return 关联记录ID
     */
    Long bindUserRole(UserRoleDTO dto);

    /**
     * 查询用户已绑定的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<UserRole> listByUserId(Long userId);

    /**
     * 删除某个用户指定的角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteUserRole(Long userId, Long roleId);

    /**
     * 删除某个用户全部角色关联
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteByUserId(Long userId);

    /**
     * 删除某个角色的全部用户关联
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * 删除某个租户下的全部用户角色关联（按租户下角色ID定位）
     *
     * @param tenantId 租户ID
     * @return 是否成功
     */
    boolean deleteByTenantId(Long tenantId);
}
