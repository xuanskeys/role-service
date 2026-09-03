package com.xuan.roleservice.service.impl;

import com.xuan.roleservice.entity.model.Permission;
import com.xuan.roleservice.mapper.PermissionMapper;
import com.xuan.roleservice.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
