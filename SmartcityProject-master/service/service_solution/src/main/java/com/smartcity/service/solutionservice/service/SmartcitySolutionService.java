package com.smartcity.service.solutionservice.service;

import com.smartcity.service.solutionservice.entity.SmartcitySolution;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 解决方案 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
public interface SmartcitySolutionService extends IService<SmartcitySolution> {
    void saveByExcel(MultipartFile file, SmartcitySolutionService service);
}
