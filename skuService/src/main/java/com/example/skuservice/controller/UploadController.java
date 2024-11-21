package com.example.skuservice.controller;

import com.example.common.domain.vo.Result;
import com.example.common.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    //接收图片或者文件
    @Value("${upload.path}")
    private String path;

    @RequestMapping(method = RequestMethod.POST)  //只允许接收POST请求
    public Result upload(MultipartFile file) {
        String fileName = UploadUtil.save(file, path);  //保存文件返回文件的名字
        return Result.success(fileName);
    }
}



