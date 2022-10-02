package com.it.service;

import com.it.dto.DishDto;

public interface DishService {
    /**
     * 新增菜品，同时插入口味数据
     */
    public  void saveWithFlavor(DishDto dishDto,long id);
}
