package com.it.service.impl;

import com.it.dto.DishDto;
import com.it.entity.DishFlavor;
import com.it.mapper.DishFlavorMapper;
import com.it.mapper.DishMapper;
import com.it.service.DishService;
import jdk.jfr.TransitionFrom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
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
}
