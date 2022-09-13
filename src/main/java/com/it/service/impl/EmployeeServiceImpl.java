package com.it.service.impl;

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
}
