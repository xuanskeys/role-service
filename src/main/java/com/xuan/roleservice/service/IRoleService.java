package com.xuan.roleservice.service;

import com.xuan.roleservice.entity.dto.RoleDTO;
import com.xuan.roleservice.entity.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据角色ID获取角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    Role getById(Long id);

    /**
     * 根据角色类型编码获取角色列表
     *
     * @param roleType 角色类型编码
     * @return 角色列表
     */
    List<Role> listByRoleType(String roleType);

    /**
     * 新增角色（同一租户下角色名称不允许重复）
     *
     * @param dto 角色入参
     * @return 新增后的角色ID
     */
    Long createRole(RoleDTO dto);

    /**
     * 编辑角色
     *
     * @param dto 角色入参（需携带 id）
     * @return 是否成功
     */
    boolean updateRole(RoleDTO dto);

    /**
     * 删除角色（逻辑删除）
     *
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long id);
}
