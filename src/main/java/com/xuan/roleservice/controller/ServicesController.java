package com.xuan.roleservice.controller;

import com.xuan.roleservice.entity.dto.ServicesDTO;
import com.xuan.roleservice.entity.model.Services;
import com.xuan.roleservice.entity.result.Result;
import com.xuan.roleservice.service.IServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 服务表 前端控制器
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@Tag(name = "服务管理", description = "服务新增/编辑/删除、按名/按ID查询、获取服务地址")
public class ServicesController {

    private final IServicesService servicesService;

    @GetMapping("/name/{serviceName}")
    @Operation(summary = "根据服务名获取服务信息")
    public Result<Services> getByServiceName(@PathVariable String serviceName) {
        return Result.success(servicesService.getByServiceName(serviceName));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据服务ID获取服务信息")
    public Result<Services> getById(@PathVariable Long id) {
        return Result.success(servicesService.getById(id));
    }

    @GetMapping("/url")
    @Operation(summary = "根据服务名获取拼接好的服务地址URL")
    public Result<String> getServiceUrl(@RequestParam String serviceName) {
        return Result.success(servicesService.getServiceUrl(serviceName));
    }

    @PostMapping
    @Operation(summary = "新增服务（服务名不允许重复）")
    public Result<Long> create(@Valid @RequestBody ServicesDTO dto) {
        try {
            return Result.success(servicesService.createService(dto));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "编辑服务")
    public Result<Boolean> update(@Valid @RequestBody ServicesDTO dto) {
        try {
            return Result.success(servicesService.updateService(dto));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除服务（逻辑删除）")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(servicesService.deleteService(id));
    }
}
