package com.smartcity.resources_crud.controller;

import com.smartcity.common.commonutils.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BaseController<E> {
    public R addByExcel(MultipartFile file);

    public R findAll();

    // public R pageAchievementCondition();

    R removeById(String id);

    public R addObject(E entity,
                       String resourceName,
                       HttpServletRequest request);

    public R getObject(String id, String resourceName, HttpServletRequest request);

    public R update(E entity, String resourceNAme);

    public R getBatch(List<String> IDList, String resourceNAme);

}
