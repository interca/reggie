package com.it.controller;

import com.it.entity.Employee;
import com.it.service.EmployeeService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口
 * @since 2022-9-14*
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public SystemJsonResponse login(HttpServletRequest request, @RequestBody Employee employee){
       //对密码进行加密处理和数据库对比
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        Employee one = employeeService.getOne(employee.getUsername());
        System.out.println(one);
        if(one==null)return SystemJsonResponse.fail("账户不存在");
        if(one.getPassword().equals(password)==false)return SystemJsonResponse.fail("密码错误");
        if(one.getStatus()==0)return SystemJsonResponse.fail("账户已禁用");
        //存入session
        request.getSession().setAttribute("employee",one);
        return SystemJsonResponse.success(one);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public  SystemJsonResponse logout(HttpServletRequest request){
        //清理session中国保存id
        request.getSession().removeAttribute("employee");
        return SystemJsonResponse.success("退出成功");
    }
}
