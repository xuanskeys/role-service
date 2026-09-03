package com.xuan.roleservice.service;

import com.xuan.roleservice.entity.dto.ServicesDTO;
import com.xuan.roleservice.entity.model.Services;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author xuan
 * @since 2026-09-03
 */
public interface IServicesService extends IService<Services> {

    /**
     * 根据服务名获取服务信息
     *
     * @param serviceName 服务名称
     * @return 服务信息
     */
    Services getByServiceName(String serviceName);

    /**
     * 根据服务ID获取服务信息
     *
     * @param id 服务ID
     * @return 服务信息
     */
    Services getById(Long id);

    /**
     * 根据服务名获取拼接好的服务地址（http://host:port）
     *
     * @param serviceName 服务名称
     * @return 服务地址 URL
     */
    String getServiceUrl(String serviceName);

    /**
     * 新增服务（服务名不允许重复）
     *
     * @param dto 服务入参
     * @return 新增后的服务ID
     */
    Long createService(ServicesDTO dto);

    /**
     * 编辑服务
     *
     * @param dto 服务入参（需携带 id）
     * @return 是否成功
     */
    boolean updateService(ServicesDTO dto);

    /**
     * 删除服务（逻辑删除）
     *
     * @param id 服务ID
     * @return 是否成功
     */
    boolean deleteService(Long id);
}
