package com.xuan.roleservice.service.impl;

import com.xuan.roleservice.entity.model.Role;
import com.xuan.roleservice.mapper.RoleMapper;
import com.xuan.roleservice.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
