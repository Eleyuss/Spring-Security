package com.gbv.webapp_gbv_l3.repository;

import com.gbv.webapp_gbv_l3.entity.Department;
import com.gbv.webapp_gbv_l3.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByDepartmentId(long departmentId);
}
