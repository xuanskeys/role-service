package com.xuan.roleservice.entity.enums;

import lombok.Getter;

/**
 * 角色类型枚举类
 * 角色类型、角色名称、角色等级
 */
@Getter
public enum RoleTypeEnum {

    // 默认角色
    DEFAULT("DEFAULT", "默认角色", 0),
    // 超管角色
    SUPER_ADMIN("SUPER_ADMIN", "超级管理员", 100),
    // 租户管理员角色
    TENANT_ADMIN("TENANT_ADMIN", "租户管理员", 90),
    // 租户角色
    TENANT("TENANT", "租户角色", 10);

    /**
     * 角色类型编码（对应 role.role_type）
     */
    private String roleType;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色等级（数值越大权限越高）
     */
    private Integer roleLevel;

    RoleTypeEnum(String roleType, String roleName, Integer roleLevel) {
        this.roleType = roleType;
        this.roleName = roleName;
        this.roleLevel = roleLevel;
    }

    /**
     * 根据角色类型编码获取枚举
     */
    public static RoleTypeEnum of(String roleType) {
        for (RoleTypeEnum value : values()) {
            if (value.roleType.equals(roleType)) {
                return value;
            }
        }
        return null;
    }
}
