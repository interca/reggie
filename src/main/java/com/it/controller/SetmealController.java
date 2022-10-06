package com.it.controller;

import com.it.service.SetmealDishService;
import com.it.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐管理
 * @author  hyj
 * @since  2022-10-7
 */
@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;


}
