package com.smartcity.resources_crud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface ResourceService<E> extends IService<E> {
    void saveByExcel(MultipartFile file, ResourceService<E> service);

}
