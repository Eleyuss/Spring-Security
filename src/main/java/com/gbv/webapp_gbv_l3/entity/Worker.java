package com.gbv.webapp_gbv_l3.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Для автоинкремента id
    private Long id;

    @Column(name = "code", length = 3) // Важно указать длину для varchar(3)
    private String code;

    @Column(name = "surname", length = 45) // Для varchar(45)
    private String surname;

    @Column(name = "name", length = 45) // Для varchar(45)
    private String name;

    @Column(name = "start_work")
    private LocalDate startWork; // Для типа данных DATE (используем LocalDate для даты без времени)

    @Column(name = "end_work")
    private LocalDate endWork; // Для типа данных DATE (используем LocalDate для даты без времени)

    @ManyToOne // Предположим, что у каждого работника есть один департамент
    @JoinColumn(name = "department", referencedColumnName = "id") // Связь с таблицей department
    private Department department;

    public Worker(String code, String surname, String name, LocalDate startWork, LocalDate endWork, Department department) {
        this.code = code;
        this.surname = surname;
        this.name = name;
        this.startWork = startWork;
        this.endWork = endWork;
        this.department = department;
    }

}

