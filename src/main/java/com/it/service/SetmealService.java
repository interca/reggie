package com.it.service;

import com.it.dto.SetmealDto;
import com.it.utli.SystemJsonResponse;

/**
 * @author  hyj
 * @since  2022-10-9
 */
public interface SetmealService {
    /**
     * 新曾套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     * @param id
     */
    void save(SetmealDto setmealDto, Long id);

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    SystemJsonResponse page(int page, int pageSize, String name);
}
