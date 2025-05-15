package com.gbv.webapp_gbv_l3.controller;


import com.gbv.webapp_gbv_l3.entity.RoleEnum;
import com.gbv.webapp_gbv_l3.entity.User;
import com.gbv.webapp_gbv_l3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Ваш шаблон регистрации
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(RoleEnum.ADMIN);

        String resultMessage = userService.saveUser(user);

        if (resultMessage.contains("already exists")) {
            model.addAttribute("errorMessage", resultMessage);
            return "register";
        }

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("message", "You have successfully logged out.");
        }
        return "login";  // Вернуть имя вашего шаблона входа
    }

}
