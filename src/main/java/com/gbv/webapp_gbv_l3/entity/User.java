package com.gbv.webapp_gbv_l3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Check(constraints = "REGEXP_LIKE(username, '[A-Za-z]+$, 'c') = 1")
    private String username;

    @Column(nullable = false)
    @Check(constraints = "REGEXP_LIKE(password, '[A-Z][0-9a-zA-Z]', 'c') = 1")
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public User(String username, String password, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
