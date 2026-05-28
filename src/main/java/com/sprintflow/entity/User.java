package com.sprintflow.entity;

import com.sprintflow.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;
}
