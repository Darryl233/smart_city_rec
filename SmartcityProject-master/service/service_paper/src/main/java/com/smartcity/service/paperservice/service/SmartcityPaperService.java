package com.smartcity.service.paperservice.service;

import com.smartcity.service.paperservice.entity.SmartcityPaper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 论文 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-05
 */
public interface SmartcityPaperService extends IService<SmartcityPaper> {
    void saveByExcel(MultipartFile file, SmartcityPaperService paperService);
}
