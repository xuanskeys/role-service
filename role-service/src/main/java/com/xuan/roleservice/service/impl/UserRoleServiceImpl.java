package com.xuan.roleservice.service.impl;

import com.xuan.roleservice.entity.model.UserRole;
import com.xuan.roleservice.mapper.UserRoleMapper;
import com.xuan.roleservice.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
