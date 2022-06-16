package com.smartcity.service.expertservice.service;

import com.smartcity.service.expertservice.entity.SmartcityExpert;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 专家 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
public interface SmartcityExpertService extends IService<SmartcityExpert> {
    public void saveByExcel(MultipartFile file, SmartcityExpertService service);
}
