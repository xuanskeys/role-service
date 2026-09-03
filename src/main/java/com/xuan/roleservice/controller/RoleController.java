package com.xuan.roleservice.controller;

import com.xuan.roleservice.entity.dto.RoleDTO;
import com.xuan.roleservice.entity.model.Role;
import com.xuan.roleservice.entity.result.Result;
import com.xuan.roleservice.service.IRoleService;
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

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色新增/编辑/删除/查询")
public class RoleController {

    private final IRoleService roleService;

    @GetMapping("/{id}")
    @Operation(summary = "根据角色ID获取角色信息")
    public Result<Role> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/type/{roleType}")
    @Operation(summary = "根据角色类型编码获取角色列表")
    public Result<List<Role>> listByRoleType(@PathVariable String roleType) {
        return Result.success(roleService.listByRoleType(roleType));
    }

    @GetMapping("/list")
    @Operation(summary = "查询全部角色（未删除）")
    public Result<List<Role>> listAll() {
        return Result.success(roleService.list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Role>()
                .eq(Role::getIsDelete, 0)));
    }

    @PostMapping
    @Operation(summary = "新增角色（同一租户下角色名称不允许重复）")
    public Result<Long> create(@Valid @RequestBody RoleDTO dto) {
        try {
            return Result.success(roleService.createRole(dto));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "编辑角色")
    public Result<Boolean> update(@Valid @RequestBody RoleDTO dto) {
        try {
            return Result.success(roleService.updateRole(dto));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色（逻辑删除）")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(roleService.deleteRole(id));
    }
}
