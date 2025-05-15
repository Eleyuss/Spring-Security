package com.gbv.webapp_gbv_l3.security;

// Импорты библиотек Spring Security и пользовательских сервисов
import com.gbv.webapp_gbv_l3.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Аннотация @Configuration указывает, что данный класс содержит конфигурацию Spring
@Configuration
// Аннотация @EnableWebSecurity активирует поддержку Spring Security для веб-приложения
@EnableWebSecurity
// Аннотация @EnableMethodSecurity включает поддержку проверки безопасности на уровне методов
@EnableMethodSecurity
public class SecurityConfig {

    // Определение бина для работы с деталями пользователя
    @Bean
    public UserDetailsService userDetailsService() {
        // Возвращаем пользовательский сервис для загрузки данных о пользователе
        return new MyUserDetailsService();
    }

    // Определение цепочки фильтров безопасности
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // Отключение защиты CSRF (для REST-API)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "register").permitAll() // Разрешаем доступ к страницам логина и регистрации
                        .anyRequest().authenticated()) // Все остальные запросы требуют аутентификации
                .formLogin(form -> form
                        .loginPage("/login") // Указываем пользовательскую страницу логина
                        .permitAll() // Разрешаем всем доступ к странице логина
                        .failureUrl("/login?error=true") // URL для обработки ошибок входа
                        .defaultSuccessUrl("/", true) // URL перенаправления после успешного входа
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Создаем сессию только при необходимости
                                .maximumSessions(1) // Ограничиваем количество сессий для одного пользователя
                                .expiredUrl("/login?expired=true") // URL перенаправления при истечении сессии
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/login?logout") // URL перенаправления после выхода
                        .invalidateHttpSession(true) // Инвалидация текущей сессии
                        .clearAuthentication(true) // Очистка аутентификации
                        .permitAll()) // Разрешаем доступ ко всем
                .build();
    }

    // Определение провайдера аутентификации
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Используем DAO-провайдер аутентификации для работы с базой данных
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // Указываем наш UserDetailsService
        provider.setPasswordEncoder(passwordEncoder()); // Указываем кодировщик паролей
        return provider;
    }

    // Определение бина для кодирования паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Возвращаем BCryptPasswordEncoder для безопасного хранения паролей
        return new BCryptPasswordEncoder();
    }
}
