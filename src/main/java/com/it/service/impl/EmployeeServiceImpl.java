package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Employee;
import com.it.mapper.EmployeeMapper;
import com.it.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
