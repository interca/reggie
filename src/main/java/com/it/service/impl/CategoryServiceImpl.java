package com.it.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.entity.Category;
import com.it.entity.Dish;
import com.it.entity.Setmeal;
import com.it.mapper.CategoryMapper;
import com.it.mapper.DishMapper;
import com.it.mapper.SetmealMapper;
import com.it.service.CategoryService;
import com.it.service.SetmealService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
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

    /**
     * 保存
     * @param category
     * @return
     */
    @Override
    public boolean save(Category category) {
        int insert = categoryMapper.insert(category);
        return insert>0;
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        //判断分类是否关联了菜品和套餐，如果是就不能删除
         LambdaQueryWrapper<Dish>dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
         dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        Integer count1 = dishMapper.selectCount(dishLambdaQueryWrapper);
        if(count1>0)return;

        LambdaQueryWrapper<Setmeal>LambdaQueryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        Integer count2 = setmealMapper.selectCount(LambdaQueryWrapper);
        if(count2>0)return;

    }


}


