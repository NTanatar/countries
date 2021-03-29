package com.tannat.country.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns        = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;
}
