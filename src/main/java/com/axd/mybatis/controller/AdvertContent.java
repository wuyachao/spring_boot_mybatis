package com.axd.mybatis.controller;

import com.axd.mybatis.model.TbAdvertContent;
import com.axd.mybatis.service.AdvertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AdvertContent {

    @Resource
    private AdvertService advertService;
    @GetMapping("/advert/content/{id}")
    public TbAdvertContent advertContents(@PathVariable int id){
        return advertService.getById(id);
    }

    @GetMapping("/advert/content/all")
    public List<TbAdvertContent> getAllAdvertContents(){
        return advertService.getAllAdvertContent();
    }
}
