package com.gbv.webapp_gbv_l3.controller;


import com.gbv.webapp_gbv_l3.entity.Department;
import com.gbv.webapp_gbv_l3.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public String home(Model model,  Authentication authentication) {
        model.addAttribute("departments", departmentService.getAllDepartments());

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);
        return "table";
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String insertDepartment(Model model,
                                   @RequestParam(value = "nameDep") String nameDep,
                                   @RequestParam(value = "nameSDep") String nameSDep,
                                   @RequestParam(value = "codeDep") String codeDep,
                                   @RequestParam(value = "emailHead") String emailHead,
                                   @RequestParam(value = "phoneHead") String phoneHead,
                                   Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        Department department = new Department();
        department.setNameDep(nameDep);
        department.setNameSDep(nameSDep);
        department.setCodeDep(codeDep);
        department.setEmailHead(emailHead);
        department.setPhoneHead(phoneHead);

        if (nameDep.isEmpty() || nameSDep.isEmpty() || codeDep.isEmpty() || emailHead.isEmpty() || phoneHead.isEmpty()) {
            model.addAttribute("message", "Ошибка: Все поля должны быть заполнены!");
            model.addAttribute("department", department);
            return "table";  // Возвращаем на страницу с формой и выводим ошибку
        }
        try {
            String message = departmentService.insertDepartment(department);
            model.addAttribute("department", department);
            model.addAttribute("message", message);
        } catch (Exception ex) {
            model.addAttribute("department", department);
            model.addAttribute("message", "Ошибка при сохранении: " + ex.getMessage());
            return "table";
        }

        model.addAttribute("departments", departmentService.getAllDepartments());
        return "table"; // Перенаправляем на главную страницу
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteDepartment(@RequestParam("id") long id, Model model, Authentication authentication) {
        // Проверяем, привязан ли департамент к каким-либо работникам
        boolean hasWorkers = departmentService.hasWorkers(id);

        if (hasWorkers) {
            model.addAttribute("message", "Cannot delete department: it is associated with workers.");
            model.addAttribute("departments", departmentService.getAllDepartments());
            String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
            model.addAttribute("username", username);
            String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                    authentication.getAuthorities().iterator().next().getAuthority() :
                    "";
            model.addAttribute("roles", roles);
            return "table"; // Возвращаемся на главную страницу с сообщением
        }

        // Если нет связанных работников, удаляем департамент
        departmentService.deleteById(id);
        return "redirect:/"; // Перенаправляем на главную страницу
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateDepartment( Model model,
                                    @RequestParam("id") long id,
                                    @RequestParam(value = "nameDep") String nameDep,
                                    @RequestParam(value = "nameSDep") String nameSDep,
                                    @RequestParam(value = "codeDep") String codeDep,
                                    @RequestParam(value = "emailHead") String emailHead,
                                    @RequestParam(value = "phoneHead") String phoneHead,
                                    Authentication authentication) {

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        Department department = departmentService.getById(id);
        department.setNameDep(nameDep);
        department.setNameSDep(nameSDep);
        department.setCodeDep(codeDep);
        department.setEmailHead(emailHead);
        department.setPhoneHead(phoneHead);

        if (nameDep.isEmpty() || nameSDep.isEmpty() || codeDep.isEmpty() || emailHead.isEmpty() || phoneHead.isEmpty()) {
            model.addAttribute("message", "Ошибка: Все поля должны быть заполнены!");
            model.addAttribute("department", department);
            return "edit";  // Возвращаем на страницу с формой и выводим ошибку
        }
        try {
            String message = departmentService.updateDepartment(department);
            if(message == "Department successfully edited!"){
                model.addAttribute("departments", departmentService.getAllDepartments());
                return "table";
            }
            else{
                model.addAttribute("department", department);
                model.addAttribute("message", message);
                return "edit";
            }
        } catch (Exception ex) {
            model.addAttribute("department", department);
            model.addAttribute("message", "Ошибка при сохранении: " + ex.getMessage());
            return "edit";
        }
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String openEditPage(@RequestParam("id") long id, Model model, Authentication authentication) {
        Department department = departmentService.getById(id);
        model.addAttribute("department", department);
        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);
        return "edit"; // Страница для редактирования
    }

    @GetMapping("/filter")
    public String findDepartments(@RequestParam(value = "nameDep") String nameDep,
                                  @RequestParam(value = "nameSDep") String nameSDep,
                                  @RequestParam(value = "codeDep") String codeDep,
                                  @RequestParam(value = "emailHead") String emailHead,
                                  @RequestParam(value = "phoneHead") String phoneHead,
                                  Model model, Authentication authentication) {

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        List<Department> departments = departmentService.getAllDepartments();
        Stream<Department> departmentStream = departments.stream();

        if (nameDep != null && !nameDep.isEmpty()) {
            departmentStream = departmentStream.filter(department -> department.getNameDep().toLowerCase().startsWith(nameDep.toLowerCase()));
        }

        // Фильтрация по краткому названию департамента
        if (nameSDep != null && !nameSDep.isEmpty()) {
            departmentStream = departmentStream.filter(department -> department.getNameSDep().toLowerCase().startsWith(nameSDep.toLowerCase()));
        }

        // Фильтрация по коду департамента
        if (codeDep != null && !codeDep.isEmpty()) {
            departmentStream = departmentStream.filter(department -> department.getCodeDep().startsWith(codeDep));
        }

        // Фильтрация по email руководителя
        if (emailHead != null && !emailHead.isEmpty()) {
            departmentStream = departmentStream.filter(department -> department.getEmailHead().toLowerCase().startsWith(emailHead.toLowerCase()));
        }

        // Фильтрация по телефону руководителя
        if (phoneHead != null && !phoneHead.isEmpty()) {
            departmentStream = departmentStream.filter(department ->  department.getPhoneHead().startsWith(phoneHead));
        }

        departments = departmentStream.collect(Collectors.toList());

        model.addAttribute("departments", departments);
        model.addAttribute("nameDep", nameDep);
        model.addAttribute("nameSDep", nameSDep);
        model.addAttribute("codeDep", codeDep);
        model.addAttribute("emailHead", emailHead);
        model.addAttribute("phoneHead", phoneHead);

        return "table"; // Отображаем отфильтрованные результаты
    }
}
