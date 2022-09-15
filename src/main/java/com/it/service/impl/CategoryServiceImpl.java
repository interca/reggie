package com.it.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.entity.Category;
import com.it.mapper.CategoryMapper;
import com.it.service.CategoryService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public SystemJsonResponse getPage(int page, int pageSize, String name) {
        Page<Category>pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category>lq=new LambdaQueryWrapper<>();
        lq.like(!StringUtils.isEmpty(name),Category::getName,name);
        lq.orderByDesc(Category::getUpdateTime);
        Page<Category> categoryPage = categoryMapper.selectPage(pageInfo, lq);
        return SystemJsonResponse.success(categoryPage);
    }

    @Override
    public boolean save(Category category) {
        int insert = categoryMapper.insert(category);
        return insert>0;
    }
}
