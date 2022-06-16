package com.smartcity.service.patentservice.service;

import com.smartcity.service.patentservice.entity.SmartcityPatent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 专利 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-14
 */
public interface SmartcityPatentService extends IService<SmartcityPatent> {

    void saveByExcel(MultipartFile file, SmartcityPatentService patentService);
}
