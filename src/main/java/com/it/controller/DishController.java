package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.dto.DishDto;
import com.it.entity.Dish;
import com.it.entity.Employee;
import com.it.mapper.DishMapper;
import com.it.service.DishFlavorService;
import com.it.service.DishService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 菜品管理
 * @since  2022-9-30
 * @author  hyj
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    DishMapper dishMapper;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public SystemJsonResponse save(HttpServletRequest request,@RequestBody DishDto dishDto){
       Employee employee = (Employee) request.getSession().getAttribute("employee");
        dishService.saveWithFlavor(dishDto,employee.getId());
        return SystemJsonResponse.success("新增菜品成功");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public  SystemJsonResponse page(int page,int pageSize,String name){
        return dishService.page(page,pageSize,name);
    }

    /**
     * 根据菜品查询信息和口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public  SystemJsonResponse get(@PathVariable Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return SystemJsonResponse.success(byIdWithFlavor);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public SystemJsonResponse update(HttpServletRequest request,@RequestBody DishDto dishDto){
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        dishService.updateWithFlavor(dishDto,employee.getId());
        return SystemJsonResponse.success("新增菜品成功");
    }


    /**
     * 获取菜品信息
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public  SystemJsonResponse list(Dish dish){
        //条件构造器
        LambdaQueryWrapper<Dish>lq=new LambdaQueryWrapper<>();
        lq.eq(Dish::getCategoryId,dish.getCategoryId());
        lq.eq(Dish::getStatus,1);
        //排序条件
        lq.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(lq);
        return SystemJsonResponse.success(dishes);
    }

}
