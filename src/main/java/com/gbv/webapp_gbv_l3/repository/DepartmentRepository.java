package com.gbv.webapp_gbv_l3.repository;

import com.gbv.webapp_gbv_l3.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameDep(String nameDep);
    boolean existsByNameSDep(String namesdep);
    boolean existsByCodeDep(String codeDep);
    boolean existsByEmailHead(String emailHead);

    // Проверка уникальности названия департамента, исключая текущую запись
    boolean existsByNameDepAndIdNot(String nameDep, Long id);

    // Проверка уникальности краткого названия департамента, исключая текущую запись
    boolean existsByNameSDepAndIdNot(String namesdep, Long id);

    // Проверка уникальности кода департамента, исключая текущую запись
    boolean existsByCodeDepAndIdNot(String codeDep, Long id);

    // Проверка уникальности email руководителя, исключая текущую запись
    boolean existsByEmailHeadAndIdNot(String emailHead, Long id);
}
