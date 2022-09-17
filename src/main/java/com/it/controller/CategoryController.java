package com.it.controller;

import com.it.entity.Category;
import com.it.entity.Employee;
import com.it.service.CategoryService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

}
