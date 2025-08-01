package com.project.emailSchedular.repository;

import com.project.emailSchedular.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Users, Long> {
}
