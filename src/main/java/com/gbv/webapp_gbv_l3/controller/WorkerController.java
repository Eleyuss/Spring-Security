package com.gbv.webapp_gbv_l3.controller;


import com.gbv.webapp_gbv_l3.entity.Department;
import com.gbv.webapp_gbv_l3.entity.Worker;
import com.gbv.webapp_gbv_l3.service.DepartmentService;
import com.gbv.webapp_gbv_l3.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/allWorkers")
    public String allWorkers(Model model, Authentication authentication) {
        model.addAttribute("workers", workerService.getAllWorkers());
        List<Department> sortedDepartments = departmentService.getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getNameDep)) // Замените getName на актуальный метод для имени отдела
                .toList();
        model.addAttribute("departments", sortedDepartments);

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        return "tableWorkers";
    }

    @PostMapping("/deleteWorker")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteWorker(@RequestParam("id") Long id) {
        workerService.deleteById(id);
        return "redirect:/allWorkers"; // Перенаправляем на главную страницу
    }

    @PostMapping("/insertWorker")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String insertWorker(Model model,
                               @RequestParam(value = "code") String code,
                               @RequestParam(value = "surname") String surname,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "startWork") String startWork,
                               @RequestParam(value = "endWork", required = false) String endWork,
                               @RequestParam(value = "department") Long departmentId,
                               Authentication authentication) {


        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);

        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        List<Department> sortedDepartments = departmentService.getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getNameDep)) // Замените getName на актуальный метод для имени отдела
                .toList();
        model.addAttribute("departments", sortedDepartments);

        Department department = departmentService.getById(departmentId);
        Worker worker = new Worker();
        worker.setCode(code);
        worker.setSurname(surname);
        worker.setName(name);
        worker.setStartWork(LocalDate.parse(startWork));
        if (endWork != null && !endWork.isEmpty()) {
            worker.setEndWork(LocalDate.parse(endWork));
            if (worker.getEndWork().isBefore(worker.getStartWork().plusMonths(3))) {
                model.addAttribute("message", "End Work Date must be at least 3 months after Start Work Date.");
                model.addAttribute("worker", worker); // Передача данных обратно в форму
                return "tableWorkers"; // Вернуться на форму с ошибкой
            }
        } else {
            worker.setEndWork(null);
        }

        worker.setDepartment(department);


        if (code.isEmpty() || surname.isEmpty() || name.isEmpty() || startWork.isEmpty() || departmentId == null) {
            model.addAttribute("message", "Ошибка: Все поля должны быть заполнены!");
            model.addAttribute("worker", worker);
            return "tableWorkers";  // Возвращаем на страницу с формой и выводим ошибку
        }

        try {
            String message = workerService.insertWorker(worker); // Assuming there's a workerService that handles the insert logic
            model.addAttribute("worker", worker);
            model.addAttribute("message", message);
        } catch (Exception ex) {
            model.addAttribute("worker", worker);
            model.addAttribute("message", "Ошибка при сохранении: " + ex.getMessage());
            return "tableWorkers";
        }

        model.addAttribute("workers", workerService.getAllWorkers()); // Assuming there's a workerService to get all workers

        return "tableWorkers"; // Перенаправляем на главную страницу
    }

    @GetMapping("/filterWorkers")
    public String findWorkers(@RequestParam(value = "surname") String surname,
                              @RequestParam(value = "name") String name,
                              @RequestParam(value = "code") String code,
                              @RequestParam(value = "startWorkFrom") String startWorkFrom,
                              @RequestParam(value = "startWorkTo") String startWorkTo,
                              @RequestParam(value = "departmentId", required = false) Long departmentId,
                              Model model,
                              Authentication authentication) {

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        List<Department> sortedDepartments = departmentService.getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getNameDep)) // Замените getName на актуальный метод для имени отдела
                .toList();

        model.addAttribute("departments", sortedDepartments);
        List<Worker> workers = workerService.getAllWorkers(); // Получаем список всех работников
        Stream<Worker> workerStream = workers.stream();

        if (departmentId != null) {
            Department department = departmentService.getById(departmentId);
            workerStream = workerStream.filter(worker -> worker.getDepartment().getNameDep().equals(department.getNameDep()));
        }
        // Фильтрация по фамилии работника
        if (surname != null && !surname.isEmpty()) {
            workerStream = workerStream.filter(worker -> worker.getSurname().toLowerCase().startsWith(surname.toLowerCase()));
        }

        // Фильтрация по имени работника
        if (name != null && !name.isEmpty()) {
            workerStream = workerStream.filter(worker -> worker.getName().toLowerCase().startsWith(name.toLowerCase()));
        }

        // Фильтрация по коду работника
        if (code != null && !code.isEmpty()) {
            workerStream = workerStream.filter(worker -> worker.getCode().startsWith(code));
        }

        // Фильтрация по дате начала работы
        if (startWorkFrom != null && !startWorkFrom.isEmpty()) {
            try {
                LocalDate startFrom = LocalDate.parse(startWorkFrom);
                workerStream = workerStream.filter(worker -> worker.getStartWork().isEqual(startFrom) || worker.getStartWork().isAfter(startFrom));
            } catch (DateTimeParseException e) {
                model.addAttribute("message", "Ошибка: Неверный формат даты начала работы (startWorkFrom)!");
                return "tableWorkers";
            }
        }

        if (startWorkTo != null && !startWorkTo.isEmpty()) {
            try {
                LocalDate startTo = LocalDate.parse(startWorkTo);
                workerStream = workerStream.filter(worker -> worker.getStartWork().isEqual(startTo) || worker.getStartWork().isBefore(startTo));
            } catch (DateTimeParseException e) {
                model.addAttribute("message", "Ошибка: Неверный формат даты начала работы (startWorkTo)!");
                return "tableWorkers";
            }
        }

        workers = workerStream.collect(Collectors.toList());

        // Добавляем результаты и фильтры в модель
        model.addAttribute("workers", workers);
        model.addAttribute("surname", surname);
        model.addAttribute("name", name);
        model.addAttribute("code", code);
        model.addAttribute("startWorkFrom", startWorkFrom);
        model.addAttribute("startWorkTo", startWorkTo);
        model.addAttribute("departmentId", departmentId);


        return "tableWorkers"; // Отображаем отфильтрованные результаты на странице
    }

    @PostMapping("/editWorker")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateWorker(Model model,
                               @RequestParam("id") Long id,
                               @RequestParam(value = "code") String code,
                               @RequestParam(value = "surname") String surname,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "startWork") String startWork,
                               @RequestParam(value = "endWork", required = false) String endWork,
                               @RequestParam(value = "department") Long department,
                               Authentication authentication) {

        String username = (authentication != null) ? authentication.getName() : ""; // Дефолтное значение
        model.addAttribute("username", username);
        String roles = authentication != null && !authentication.getAuthorities().isEmpty() ?
                authentication.getAuthorities().iterator().next().getAuthority() :
                "";
        model.addAttribute("roles", roles);

        List<Department> sortedDepartments = departmentService.getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getNameDep)) // Замените getName на актуальный метод для имени отдела
                .toList();

        model.addAttribute("departments", sortedDepartments);
        Worker worker = workerService.getById(id); // Получаем работника по id
        worker.setCode(code);
        worker.setSurname(surname);
        worker.setName(name);

        try {
            // Преобразуем строки в даты
            LocalDate startDate = LocalDate.parse(startWork);
            worker.setStartWork(startDate);

            if (endWork != null && !endWork.isEmpty()) {
                worker.setEndWork(LocalDate.parse(endWork)); // Устанавливаем дату окончания
                if (worker.getEndWork().isBefore(worker.getStartWork().plusMonths(3))) {
                    model.addAttribute("message", "End Work Date must be at least 3 months after Start Work Date.");
                    model.addAttribute("worker", worker); // Передача данных обратно в форму
                    return "editWorker"; // Вернуться на форму с ошибкой
                }
            } else {
                worker.setEndWork(null); // Если не указана дата окончания, то null
            }

        } catch (DateTimeParseException e) {
            model.addAttribute("message", "Ошибка: Неверный формат даты!");
            model.addAttribute("worker", worker);
            return "edit";  // Возвращаем на страницу с ошибкой
        }

        Department department1 = departmentService.getById(department);
        worker.setDepartment(department1); // Устанавливаем департамент

        // Проверяем, что все обязательные поля заполнены
        if (code.isEmpty() || surname.isEmpty() || name.isEmpty() || startWork.isEmpty() || department == 0) {
            model.addAttribute("message", "Ошибка: Все поля должны быть заполнены!");
            model.addAttribute("worker", worker);
            return "editWorker";  // Возвращаем на страницу с формой и выводим ошибку
        }

        try {
            String message = workerService.updateWorker(worker);  // Обновляем работника
            if ("Worker successfully edited!".equals(message)) {
                model.addAttribute("workers", workerService.getAllWorkers());
                return "tableWorkers";  // Возвращаем на страницу с таблицей работников
            } else {
                model.addAttribute("worker", worker);
                model.addAttribute("message", message);
                return "editWorker";  // Если возникла ошибка при сохранении, возвращаем на страницу редактирования
            }
        } catch (Exception ex) {
            model.addAttribute("worker", worker);
            model.addAttribute("message", "Ошибка при сохранении: " + ex.getMessage());
            return "editWorker";  // Возвращаем на страницу редактирования с ошибкой
        }
    }

    @GetMapping("/editWorker")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String openEditPage(@RequestParam("id") Long id, Model model) {
        Worker worker = workerService.getById(id);  // Получаем работника по id
        model.addAttribute("worker", worker);  // Добавляем данные работника в модель
        List<Department> sortedDepartments = departmentService.getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getNameDep)) // Замените getName на актуальный метод для имени отдела
                .toList();

        model.addAttribute("departments", sortedDepartments);
        return "editWorker";  // Страница для редактирования работника
    }

}
