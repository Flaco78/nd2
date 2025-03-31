package org.example.snowflake_nd2.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "USER_ID_SEQ", allocationSize = 1)
    private Long id;

    private String name;
    private String email;
    private Integer age;
}
