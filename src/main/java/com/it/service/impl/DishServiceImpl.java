package com.it.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.dto.DishDto;
import com.it.entity.Category;
import com.it.entity.Dish;
import com.it.entity.DishFlavor;
import com.it.entity.Employee;
import com.it.mapper.CategoryMapper;
import com.it.mapper.DishFlavorMapper;
import com.it.mapper.DishMapper;
import com.it.service.DishService;
import com.it.utli.SystemJsonResponse;
import jdk.jfr.TransitionFrom;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto,long id) {
        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setUpdateTime(LocalDateTime.now());
        dishDto.setUpdateUser(id);
        dishDto.setCreateUser(id);
        dishMapper.insert(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor k:flavors){
            k.setDishId(dishDto.getId());
            k.setCreateTime(LocalDateTime.now());
            k.setUpdateTime(LocalDateTime.now());
            k.setUpdateUser(id);
            k.setCreateUser(id);
            dishFlavorMapper.insert(k);
        }

    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public SystemJsonResponse page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo =new Page<>(page,pageSize);
        Page<DishDto> pageInfo2 =new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> lq =new LambdaQueryWrapper<>();

        //添加过滤条件
        lq.like(name!=null,Dish::getName,name);

        //排序条件
        lq.orderByDesc(Dish::getUpdateTime);
         dishMapper.selectPage(pageInfo, lq);

        //对象拷贝  忽略records
        BeanUtils.copyProperties(pageInfo,pageInfo2,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        for(Dish d:records){
            Long id = d.getCategoryId();
            //根据id查询分类对象
            Category category = categoryMapper.selectById(id);
            String name1 = category.getName();
            DishDto dishDto=new DishDto();
            dishDto.setCategoryName(name1);
            //对象拷贝
            BeanUtils.copyProperties(d,dishDto);
            list.add(dishDto);
        }
        pageInfo2.setRecords(list);
        return SystemJsonResponse.success(pageInfo2);
    }


    /**
     * 根据id查询菜品信息和口味
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息
        Dish dish = dishMapper.selectById(id);

        //对象拷贝
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询口味信息
        LambdaQueryWrapper<DishFlavor>lq=new LambdaQueryWrapper<>();
        lq.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lq);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }
}
