package com.xuan.roleservice.entity.dto;

import com.xuan.roleservice.entity.enums.RoleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色新增/编辑入参
 */
@Data
@Schema(description = "角色新增/编辑入参")
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID，编辑时必填")
    private Long id;

    @NotNull(message = "角色类型不能为空")
    @Schema(description = "角色类型编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private RoleTypeEnum roleType;

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    @NotNull(message = "租户ID不能为空")
    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tenantId;

    @Schema(description = "角色等级（数值越大权限越高）")
    private Integer roleLevel;
}
