package com.project.emailSchedular.repository;

import com.project.emailSchedular.entity.EmailCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCredentialRepository extends JpaRepository<EmailCredential, Long> {
}
