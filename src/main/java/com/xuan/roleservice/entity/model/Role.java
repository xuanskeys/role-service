package com.xuan.roleservice.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private String roleType;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 租户ID，逻辑关联user_service.tenant.id
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 角色等级
     */
    @TableField("role_level")
    private Integer roleLevel;

    /**
     * 是否逻辑删除：0否，1是
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 删除时间
     */
    @TableField("delete_time")
    private LocalDateTime deleteTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
