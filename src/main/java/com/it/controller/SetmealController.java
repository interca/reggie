package com.it.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.it.dto.SetmealDto;
import com.it.entity.Employee;
import com.it.service.SetmealDishService;
import com.it.service.SetmealService;
import com.it.utli.SystemJsonResponse;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

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


    /**
     * 新增套餐接口
     * @param setmealDto
     * @return
     */
     @PostMapping
     public SystemJsonResponse save(HttpServletRequest httpServletRequest, @RequestBody SetmealDto setmealDto){
         Employee employee = (Employee) httpServletRequest.getSession().getAttribute("employee");
         Long id = employee.getId();
         setmealService.save(setmealDto,id);
         return SystemJsonResponse.success();
     }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
     @GetMapping("/page")
     public  SystemJsonResponse page(int page ,int pageSize,String name){
          return  setmealService.page(page,pageSize,name);
     }


    /**
     * 删除菜品
     * @param ids
     * @return
     */
     @DeleteMapping
     public  SystemJsonResponse delete(@RequestParam List<Long>ids){
          setmealService.removeWithDish(ids);
          return SystemJsonResponse.success("删除成功");
     }
}
