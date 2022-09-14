package com.it.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Employee;
import com.it.mapper.EmployeeMapper;
import com.it.service.EmployeeService;
import com.it.utli.SystemJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工service实现类
 * @since 2022-9-14
 * @author hyj*
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public Employee getOne(String name) {
        LambdaQueryWrapper<Employee>lq=new LambdaQueryWrapper<>();
        lq.eq(Employee::getUsername,name);
        Employee employee = employeeMapper.selectOne(lq);
        return employee;
    }

    @Override
    public boolean save(Employee employee) {
        int insert = employeeMapper.insert(employee);
        return insert>0;
    }

    @Override
    public SystemJsonResponse get(int page, int pageSize, String name) {
         //分页构造器
        Page<Employee>pageInfo =new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lq=new LambdaQueryWrapper();
        //过滤条件
        lq.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        lq.orderByDesc(Employee::getUpdateTime);
        Page<Employee> employeePage = employeeMapper.selectPage(pageInfo, lq);
        List<Employee> records = employeePage.getRecords();
        for(Employee e:records){
            System.out.println(e);
        }
        return  SystemJsonResponse.success(employeePage);
    }
}
