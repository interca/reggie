package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.entity.Category;
import com.it.entity.Employee;
import com.it.mapper.CategoryMapper;
import com.it.service.CategoryService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类接口
 * @since 2022-9-16
 * @author hyj
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品或者套餐分类
     * @param category
     * @return
     */
    @PostMapping
    public SystemJsonResponse save(HttpServletRequest request, @RequestBody Category category){
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        System.out.println(category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(employee.getId());
        category.setUpdateUser(employee.getId());
        categoryService.save(category);
        return SystemJsonResponse.success(1,"新增成功");
    }

    /**
     * 查询分页功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public SystemJsonResponse getEmployee(int page,int pageSize,String name){
        SystemJsonResponse systemJsonResponse = categoryService.getPage(page, pageSize, name);
        return systemJsonResponse;
    }


    /**
     * 删除菜品或者套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public  SystemJsonResponse delete(Long ids){
        categoryService.remove(ids);
        return SystemJsonResponse.success();
    }

    /**
     * 更新套餐或者菜品
     * @param request
     * @param category
     * @return
     */
    @PutMapping
    public SystemJsonResponse update(HttpServletRequest request, @RequestBody Category category){
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(employee.getId());
        categoryService.update(category);
         return SystemJsonResponse.success();
    }


    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public  SystemJsonResponse list(Category category){
        LambdaQueryWrapper<Category>lq=new LambdaQueryWrapper<>();
        //添加条件
         lq.eq(category.getType() != null, Category::getType, category.getType());
         //按顺序分类
        lq.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(lq);
        return SystemJsonResponse.success(categories);
    }

}

