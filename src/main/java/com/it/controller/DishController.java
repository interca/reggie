package com.it.controller;

import com.it.dto.DishDto;
import com.it.entity.Employee;
import com.it.service.DishFlavorService;
import com.it.service.DishService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}
