package com.axd.mybatis.service;

import com.axd.mybatis.mapper.TbAdvertContentMapper;
import com.axd.mybatis.model.TbAdvertContent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdvertService {

    @Resource
    private TbAdvertContentMapper advertContentMapper;

    public List<TbAdvertContent> getAllAdvertContent() {
        return advertContentMapper.selectAll();
    }

    public TbAdvertContent getById(Integer id) {
        return advertContentMapper.getById(id);
    }
}
