package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.Exception.CustomException;
import com.it.dto.SetmealDto;
import com.it.entity.Category;
import com.it.entity.Setmeal;
import com.it.entity.SetmealDish;
import com.it.mapper.CategoryMapper;
import com.it.mapper.SetmealDishMapper;
import com.it.mapper.SetmealMapper;
import com.it.service.SetmealService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐service
 * @since  2022-10-9
 * @author  hyj
 */
@Service
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新曾套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     * @param id
     */
    @Transactional
    @Override
    public void save(SetmealDto setmealDto, Long id) {
         //保存套餐的基本信息
         setmealDto.setCreateTime(LocalDateTime.now());
         setmealDto.setUpdateTime(LocalDateTime.now());
         setmealDto.setUpdateUser(id);
         setmealDto.setCreateUser(id);
         setmealMapper.insert(setmealDto);

        //套餐和菜品的关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for(SetmealDish s:setmealDishes){
             s.setCreateTime(LocalDateTime.now());
             s.setUpdateTime(LocalDateTime.now());
             s.setUpdateUser(id);
             s.setCreateUser(id);
             s.setSetmealId(setmealDto.getId());
             setmealDishMapper.insert(s);
        }

    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public SystemJsonResponse page(int page, int pageSize, String name) {
        //分页构造器
        Page<Setmeal>pageInfo =new Page<>(page,pageSize);
        Page<SetmealDto>setmealDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Setmeal>lq=new LambdaQueryWrapper<>();
        //模糊查询
        lq.like(name!=null,Setmeal::getName,name);

        //排序条件
        lq.orderByDesc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(pageInfo,lq);

        //对象拷贝 忽略records
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=new ArrayList<>();
        for(Setmeal s:records){
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(s,setmealDto);
            //获取分类id查询分类数据
            Long categoryId = s.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if(category!=null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            list.add(setmealDto);
        }
        setmealDtoPage.setRecords(list);
        return SystemJsonResponse.success(setmealDtoPage);
    }



    /**
     * 删除套餐和他关联的菜品
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态 看是否可以删除
        LambdaQueryWrapper<Setmeal>lq = new LambdaQueryWrapper<>();
        //in 查询符号ids的套餐
        lq.in(Setmeal::getId,ids);
        lq.eq(Setmeal::getStatus,1);

        Integer count = setmealMapper.selectCount(lq);
        //不能删除  报异常
        if(count>0){
            throw  new CustomException("套餐在售卖,不能删除");
        }

        //符号就删除
        setmealMapper.deleteBatchIds(ids);

        //删除关系表的数据
        LambdaQueryWrapper<SetmealDish>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishMapper.delete(lambdaQueryWrapper);
    }
}
