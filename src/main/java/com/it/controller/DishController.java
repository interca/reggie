package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.dto.DishDto;
import com.it.entity.Category;
import com.it.entity.Dish;
import com.it.entity.DishFlavor;
import com.it.entity.Employee;
import com.it.mapper.CategoryMapper;
import com.it.mapper.DishFlavorMapper;
import com.it.mapper.DishMapper;
import com.it.service.DishFlavorService;
import com.it.service.DishService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

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
   /* @GetMapping("/list")
    public  SystemJsonResponse list(Dish dish){
        //条件构造器
        LambdaQueryWrapper<Dish>lq=new LambdaQueryWrapper<>();
        lq.eq(Dish::getCategoryId,dish.getCategoryId());
        lq.eq(Dish::getStatus,1);
        //排序条件
        lq.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(lq);
        return SystemJsonResponse.success(dishes);
    }*/

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
        //返回口味信息
        List<DishDto> dishDtos = new ArrayList<>();
        //信息复制
        for(Dish k:dishes){
            LambdaQueryWrapper<Category> cat = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<DishFlavor> flavor = new LambdaQueryWrapper<>();
            cat.eq(Category::getId, k.getCategoryId());
            Category category = categoryMapper.selectOne(cat);
            flavor.eq(DishFlavor::getDishId,k.getId());
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(flavor);
            DishDto dishDto =new DishDto();
            BeanUtils.copyProperties(k,dishDto);
            dishDto.setFlavors(dishFlavors);
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        }
        return SystemJsonResponse.success(dishDtos);
    }

    @PostMapping("/status/{status}")
    public  SystemJsonResponse status(HttpServletRequest request ,@PathVariable Integer status, @RequestParam("ids") String id){
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        Dish dish =new Dish();
        dish.setStatus(status);
        dish.setId(Long.valueOf(id));
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(employee.getId());
        dishMapper.updateById(dish);
        return SystemJsonResponse.success("修改成功");
    }
}
