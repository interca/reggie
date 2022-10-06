package com.it.service;

import com.it.dto.DishDto;
import com.it.utli.SystemJsonResponse;

public interface DishService {
    /**
     * 新增菜品，同时插入口味数据
     */
    public  void saveWithFlavor(DishDto dishDto,long id);

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    SystemJsonResponse page(int page, int pageSize, String name);

    /**
     * 根据id查询菜品信息和口味
     * @param id
     * @return
     */
    public  DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     * @param id
     */
    void updateWithFlavor(DishDto dishDto, Long id);
}
