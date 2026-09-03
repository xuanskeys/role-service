package com.xuan.roleservice.mapper;

import com.xuan.roleservice.entity.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据租户ID查询该租户下全部角色ID
     *
     * @param tenantId 租户ID
     * @return 角色ID列表
     */
    List<Long> selectIdsByTenantId(@Param("tenantId") Long tenantId);
}
