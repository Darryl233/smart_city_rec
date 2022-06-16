package com.smartcity.service.requirementservice.service;

import com.smartcity.service.requirementservice.entity.SmartcityRequirement;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 项目需求 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
public interface SmartcityRequirementService extends IService<SmartcityRequirement> {
    void saveByExcel(MultipartFile file, SmartcityRequirementService requirementService);
}
