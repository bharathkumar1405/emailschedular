package com.project.emailSchedular.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EmailCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String smtpHost;
    private Integer smtpPort;
    private Boolean useSsl;
}
