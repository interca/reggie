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

/**
 * ่ๅ
 * @author  hyj
 * @since  2022-10-2
 */
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
        System.out.println(dishDto.getImage());
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
     * ๅ้กต
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public SystemJsonResponse page(int page, int pageSize, String name) {
        //ๆ้?ๅ้กตๆ้?ๅจ
        Page<Dish> pageInfo =new Page<>(page,pageSize);
        Page<DishDto> pageInfo2 =new Page<>(page,pageSize);

        //ๆกไปถๆ้?ๅจ
        LambdaQueryWrapper<Dish> lq =new LambdaQueryWrapper<>();

        //ๆทปๅ?่ฟๆปคๆกไปถ
        lq.like(name!=null,Dish::getName,name);

        //ๆๅบๆกไปถ
        lq.orderByDesc(Dish::getUpdateTime);
         dishMapper.selectPage(pageInfo, lq);

        //ๅฏน่ฑกๆท่ด  ๅฟฝ็ฅrecords
        BeanUtils.copyProperties(pageInfo,pageInfo2,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        for(Dish d:records){
            Long id = d.getCategoryId();
            //ๆ?นๆฎidๆฅ่ฏขๅ็ฑปๅฏน่ฑก
            Category category = categoryMapper.selectById(id);
            String name1 = category.getName();
            DishDto dishDto=new DishDto();
            dishDto.setCategoryName(name1);
            //ๅฏน่ฑกๆท่ด
            BeanUtils.copyProperties(d,dishDto);
            list.add(dishDto);
        }
        pageInfo2.setRecords(list);
        return SystemJsonResponse.success(pageInfo2);
    }


    /**
     * ๆ?นๆฎidๆฅ่ฏข่ๅไฟกๆฏๅๅฃๅณ
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //ๆฅ่ฏข่ๅไฟกๆฏ
        Dish dish = dishMapper.selectById(id);

        //ๅฏน่ฑกๆท่ด
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //ๆฅ่ฏขๅฃๅณไฟกๆฏ
        LambdaQueryWrapper<DishFlavor>lq=new LambdaQueryWrapper<>();
        lq.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lq);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * ๆดๆฐ่ๅไฟกๆฏๅๅฃๅณไฟกๆฏ
     * @param dishDto
     * @param id
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto, Long id) {
        //ๆดๆฐdish่กจ
        dishDto.setUpdateTime(LocalDateTime.now());
        dishDto.setUpdateUser(id);
        dishMapper.updateById(dishDto);

        //ๆดๆฐdishFlavor่กจ
        //ๅๅ?้คๅฃๅณ่กจๅฏนๅบ็ๆฐๆฎ
        LambdaQueryWrapper<DishFlavor>lq=new LambdaQueryWrapper<>();
        lq.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(lq);

        //ๅๆฐๅขๅฃๅณ
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
}

