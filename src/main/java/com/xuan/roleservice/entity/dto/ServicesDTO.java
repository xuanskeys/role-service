package com.xuan.roleservice.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务新增/编辑入参
 */
@Data
@Schema(description = "服务新增/编辑入参")
public class ServicesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "服务ID，编辑时必填")
    private Long id;

    @NotBlank(message = "服务名称不能为空")
    @Schema(description = "服务名称（唯一）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String serviceName;

    @NotBlank(message = "主机不能为空")
    @Schema(description = "主机地址，如 127.0.0.1 或 example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String host;

    @NotBlank(message = "端口不能为空")
    @Schema(description = "端口，如 8080", requiredMode = Schema.RequiredMode.REQUIRED)
    private String port;
}
