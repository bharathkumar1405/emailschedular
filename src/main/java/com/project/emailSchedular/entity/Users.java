package com.project.emailSchedular.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime emailDate;
    private String subject;
    private String content;
    private String email;
    private String cc;
    private String bcc;
    private String template;
    private String status;
}
