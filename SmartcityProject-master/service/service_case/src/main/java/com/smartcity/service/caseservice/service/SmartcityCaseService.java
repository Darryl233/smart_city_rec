package com.smartcity.service.caseservice.service;

import com.smartcity.service.caseservice.entity.SmartcityCase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 案例 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-26
 */
public interface SmartcityCaseService extends IService<SmartcityCase> {
    public void saveByExcel(MultipartFile file, SmartcityCaseService service);
}
