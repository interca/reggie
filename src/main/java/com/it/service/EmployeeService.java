package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Employee;
import com.it.utli.SystemJsonResponse;

/**
 * 员工service接口
 * @author hyj
 * @since 2022-9-14
 */
public interface EmployeeService extends IService<Employee> {
    Employee getOne(String name);

    boolean save(Employee employee);

    SystemJsonResponse get(int page, int pageSize, String name);
}
