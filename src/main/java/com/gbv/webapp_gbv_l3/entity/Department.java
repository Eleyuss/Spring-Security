package com.gbv.webapp_gbv_l3.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.Length;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(
        uniqueConstraints = {@UniqueConstraint(
                name = "check03",
                columnNames = {"name_dep"}
        ), @UniqueConstraint(
                name = "check04",
                columnNames = {"name_sdep"}
        ), @UniqueConstraint(
                name = "check05",
                columnNames = {"code_dep"}
        ), @UniqueConstraint(
                name = "check06",
                columnNames = {"email_head"}
        )}
)
public class Department {
    public static final String NAMESDEP_ERR_MSG = "Абревіатура не повинна бути довша за 8 символів";
    public static final String CODEDEP_ERR_MSG = "Код має містити три символи";
    public static final String PHONE_MASK_ERR_MSG = "Телефон має бути формату NN-NNN";
    public static final String EMAIL_ERR_MSG = "Імейл повинен бути валідним";
    public static final String ID_FIELD = "id";
    public static final String NAMEDEP_FIELD = "nameDep";
    public static final String NAMESDEP_FIELD = "nameSDep";
    public static final String CODEDEP_FIELD = "codeDep";
    public static final String EMAIL_HEAD_FIELD = "emailHead";
    public static final String PHONE_HEAD_FIELD = "phoneHead";
    public static final Map<String, String> CONSTRAINS_ERR_MESSAGES = Map.of("Department.check01", "Імейл повинен бути валідним", "Department.check02", "Телефон має бути формату NN-NNN", "Department.check03", "Назва департаменту повинна бути унікальна", "Department.check04", "Скорочена назва департаменту повинна бути унікальна", "Department.check05", "Код департаменту повинен бути унікальним", "Department.check06", "Імейл повинен бути унікальним", "Department.check07", "Код має містити три символи");
    @Id
    @GeneratedValue
    private Long id;
    @Column(
            nullable = false,
            length = 128
    )
    private String nameDep;
    @Column(
            nullable = false,
            length = 8
    )
    private @Length(
            max = 8,
            message = "Абревіатура не повинна бути довша за 8 символів"
    ) String nameSDep;
    @Column(
            nullable = false,
            length = 3
    )
    @Check(
            constraints = "codeDep REGEXP '^\\[0-9]{3}$'",
            name = "check07"
    )
    private @Length(
            max = 3,
            message = "Код має містити три символи"
    ) String codeDep;
    @Column(
            nullable = false,
            length = 64
    )
    @Check(
            constraints = "emailHead REGEXP '^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$'",
            name = "check01"
    )
    private @Email(
            message = "Імейл повинен бути валідним"
    ) String emailHead;
    @Column(
            nullable = false,
            length = 6
    )
    @Check(
            constraints = "phoneHead REGEXP '^[0-9]{2}-[0-9]{3}$'",
            name = "check02"
    )
    private @Pattern(
            regexp = "^[0-9]{2}-[0-9]{3}$",
            message = "Телефон має бути формату NN-NNN"
    ) String phoneHead;
    @Generated
    public Department(String nameDep, String nameSDep, String codeDep, String emailHead, String phoneHead) {
        this.nameDep = nameDep;
        this.nameSDep = nameSDep;
        this.codeDep = codeDep;
        this.emailHead = emailHead;
        this.phoneHead = phoneHead;
    }
}
