package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Employee;

/**
 * 员工service接口
 * @author hyj
 * @since 2022-9-14
 */
public interface EmployeeService extends IService<Employee> {
    Employee getOne(String name);

    boolean save(Employee employee);
}
