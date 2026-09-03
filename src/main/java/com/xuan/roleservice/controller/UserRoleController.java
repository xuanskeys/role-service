package com.xuan.roleservice.controller;

import com.xuan.roleservice.entity.dto.UserRoleDTO;
import com.xuan.roleservice.entity.model.UserRole;
import com.xuan.roleservice.entity.result.Result;
import com.xuan.roleservice.service.IUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 前端控制器
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@RestController
@RequestMapping("/user-role")
@RequiredArgsConstructor
@Tag(name = "用户角色关联", description = "绑定/解绑用户角色、按用户/租户/角色维度删除关联")
public class UserRoleController {

    private final IUserRoleService userRoleService;

    @PostMapping("/bind")
    @Operation(summary = "绑定用户角色（同一用户同一角色不重复）")
    public Result<Long> bind(@Valid @RequestBody UserRoleDTO dto) {
        try {
            return Result.success(userRoleService.bindUserRole(dto));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户已绑定的角色列表")
    public Result<List<UserRole>> listByUser(@PathVariable Long userId) {
        return Result.success(userRoleService.listByUserId(userId));
    }

    @DeleteMapping("/user/{userId}/role/{roleId}")
    @Operation(summary = "删除某个用户的指定角色关联")
    public Result<Boolean> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return Result.success(userRoleService.deleteUserRole(userId, roleId));
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "删除某个用户的全部角色关联")
    public Result<Boolean> deleteByUser(@PathVariable Long userId) {
        return Result.success(userRoleService.deleteByUserId(userId));
    }

    @DeleteMapping("/role/{roleId}")
    @Operation(summary = "删除某个角色的全部用户关联")
    public Result<Boolean> deleteByRole(@PathVariable Long roleId) {
        return Result.success(userRoleService.deleteByRoleId(roleId));
    }

    @DeleteMapping("/tenant/{tenantId}")
    @Operation(summary = "删除某个租户下的全部用户角色关联")
    public Result<Boolean> deleteByTenant(@PathVariable Long tenantId) {
        return Result.success(userRoleService.deleteByTenantId(tenantId));
    }
}
