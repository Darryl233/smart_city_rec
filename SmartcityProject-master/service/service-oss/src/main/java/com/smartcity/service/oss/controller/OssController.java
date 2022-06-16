package com.smartcity.service.oss.controller;

import com.smartcity.common.commonutils.R;
import com.smartcity.service.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像
    @PostMapping("/avataross")
    public R uploadOssAvatar(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "avatar");
        return R.ok().message("文件上传成功").data("url", url);
    }

    //上传论文
    @PostMapping("/papeross")
    public R uploadOssPaper(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "paper");
        return R.ok().message("文件上传成功").data("url", url);
    }

    //上传专利
    @PostMapping("/patentoss")
    public R uploadOssPatent(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "patent");
        return R.ok().message("文件上传成功").data("url", url);
    }

    //上传成果
    @PostMapping("/achievementoss")
    public R uploadOssAchievement(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "achievement");
        return R.ok().message("文件上传成功").data("url", url);
    }

    @PostMapping("/requirementoss")
    public R uploadOssRequirement(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "requirement");
        return R.ok().message("文件上传成功").data("url", url);
    }

    @PostMapping("/solutionoss")
    public R uploadOssSolution(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "solution");
        return R.ok().message("文件上传成功").data("url", url);
    }

    @PostMapping("/caseoss")
    public R uploadOssCase(MultipartFile file){
        //获取上传文件multipartfile
        //返回上传到oss的文件路径
        String url = ossService.uploadFile(file, "case");
        return R.ok().message("文件上传成功").data("url", url);
    }
}
