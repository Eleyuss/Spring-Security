package com.gbv.webapp_gbv_l3.service;


import com.gbv.webapp_gbv_l3.entity.Department;
import com.gbv.webapp_gbv_l3.repository.DepartmentRepository;
import com.gbv.webapp_gbv_l3.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private WorkerRepository workerRepository;
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public String insertDepartment(Department department) {
        // Проверка на уникальность name_dep, namesdep, code_dep и email_head
        if (departmentRepository.existsByNameDep(department.getNameDep())) {
            return "Error: Department name already exists!";
        }
        if (departmentRepository.existsByNameSDep(department.getNameSDep())) {
            return "Error: Department short name already exists!";
        }
        if (departmentRepository.existsByCodeDep(department.getCodeDep())) {
            return "Error: Department code already exists!";
        }
        if (departmentRepository.existsByEmailHead(department.getEmailHead())) {
            return "Error: Department head email already exists!";
        }

        try {
            departmentRepository.save(department);
        } catch (Exception e) {
            return "Error inserting department: " + e.getMessage();
        }

        return "Department successfully added!";
    }

    @Override
    public String updateDepartment(Department department) {
        if (departmentRepository.existsByNameDepAndIdNot(department.getNameDep(), department.getId())) {
            return "Error: Department name already exists!";
        }
        if (departmentRepository.existsByNameSDepAndIdNot(department.getNameSDep(), department.getId())) {
            return "Error: Department short name already exists!";
        }
        if (departmentRepository.existsByCodeDepAndIdNot(department.getCodeDep(), department.getId())) {
            return "Error: Department code already exists!";
        }
        if (departmentRepository.existsByEmailHeadAndIdNot(department.getEmailHead(), department.getId())) {
            return "Error: Department head email already exists!";
        }
        try{
            departmentRepository.save(department);
        }catch (Exception e) {
            return "Error updating department: " + e.getMessage();
        }
        return "Department successfully edited!";

    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.getById(id);
    }
    public boolean hasWorkers(long departmentId) {
        // Проверяем, есть ли работники, связанные с этим департаментом
        return workerRepository.existsByDepartmentId(departmentId);
    }
    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
