package com.smartcity.service.domainservice.service;

import com.smartcity.service.domainservice.entity.Domain;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 领域类别 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
public interface DomainService extends IService<Domain> {

    void saveByExcel(MultipartFile file, DomainService domainService);
}
