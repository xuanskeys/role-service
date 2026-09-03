package com.xuan.roleservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.roleservice.entity.dto.ServicesDTO;
import com.xuan.roleservice.entity.model.Services;
import com.xuan.roleservice.mapper.ServicesMapper;
import com.xuan.roleservice.service.IServicesService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
@Service
public class ServicesServiceImpl extends ServiceImpl<ServicesMapper, Services> implements IServicesService {

    private static final int NOT_DELETED = 0;

    @Override
    public Services getByServiceName(String serviceName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Services>()
                .eq(Services::getServiceName, serviceName)
                .eq(Services::getIsDelete, NOT_DELETED));
    }

    @Override
    public Services getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public String getServiceUrl(String serviceName) {
        Services services = getByServiceName(serviceName);
        if (services == null) {
            return null;
        }
        return buildUrl(services.getHost(), services.getPort());
    }

    @Override
    public Long createService(ServicesDTO dto) {
        // 服务名不允许重复
        if (getByServiceName(dto.getServiceName()) != null) {
            throw new IllegalArgumentException("服务名称已存在：" + dto.getServiceName());
        }
        Services entity = new Services();
        BeanUtils.copyProperties(dto, entity);
        entity.setIsDelete(NOT_DELETED);
        entity.setCreateTime(LocalDateTime.now());
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean updateService(ServicesDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("编辑服务时服务ID不能为空");
        }
        Services exist = baseMapper.selectById(dto.getId());
        if (exist == null || NOT_DELETED != exist.getIsDelete()) {
            throw new IllegalArgumentException("服务不存在：" + dto.getId());
        }
        // 改名时需保证新名称不与其它服务冲突
        Services sameName = getByServiceName(dto.getServiceName());
        if (sameName != null && !sameName.getId().equals(dto.getId())) {
            throw new IllegalArgumentException("服务名称已存在：" + dto.getServiceName());
        }
        BeanUtils.copyProperties(dto, exist);
        exist.setCreateTime(null);
        return baseMapper.updateById(exist) > 0;
    }

    @Override
    public boolean deleteService(Long id) {
        Services exist = baseMapper.selectById(id);
        if (exist == null) {
            return false;
        }
        exist.setIsDelete(1);
        exist.setCreateTime(null);
        return baseMapper.updateById(exist) > 0;
    }

    /**
     * 拼接服务地址：http://host:port（端口为空时仅返回 http://host）
     */
    private String buildUrl(String host, String port) {
        if (host == null) {
            return null;
        }
        String trimmedHost = host.trim();
        if (trimmedHost.startsWith("http://") || trimmedHost.startsWith("https://")) {
            return port == null || port.isBlank() ? trimmedHost : trimmedHost + ":" + port.trim();
        }
        return port == null || port.isBlank()
                ? "http://" + trimmedHost
                : "http://" + trimmedHost + ":" + port.trim();
    }
}
