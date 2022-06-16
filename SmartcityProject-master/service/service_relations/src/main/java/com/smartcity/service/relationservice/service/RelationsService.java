package com.smartcity.service.relationservice.service;

import com.smartcity.service.relationservice.entity.Relations;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 关系表：单位-提出-案例 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-28
 */
public interface RelationsService extends IService<Relations> {
    public void saveByExcel(MultipartFile file, RelationsService service);
}
