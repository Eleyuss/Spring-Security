package com.gbv.webapp_gbv_l3.service;

import com.gbv.webapp_gbv_l3.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    String insertDepartment(Department department);
    String updateDepartment(Department department);
    boolean hasWorkers(long departmentId);
    Department getById(Long id);
    void deleteById(Long id);
}
