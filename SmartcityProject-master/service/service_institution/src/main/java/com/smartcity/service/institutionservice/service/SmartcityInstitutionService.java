package com.smartcity.service.institutionservice.service;

import com.smartcity.service.institutionservice.entity.SmartcityInstitution;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 单位 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-20
 */
public interface SmartcityInstitutionService extends IService<SmartcityInstitution> {
    void saveByExcel(MultipartFile file, SmartcityInstitutionService institutionService);
}
