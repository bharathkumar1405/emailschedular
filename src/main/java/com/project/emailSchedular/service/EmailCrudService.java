package com.project.emailSchedular.service;

import com.project.emailSchedular.entity.Users;
import com.project.emailSchedular.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmailCrudService {
    @Autowired
    private EmailRepository emailRepository;

    public List<Users> getAllEmails() {
        return emailRepository.findAll();
    }

    public Optional<Users> getEmailById(Long id) {
        return emailRepository.findById(id);
    }

    public Users createEmail(Users users) {
        return emailRepository.save(users);
    }

    public Users updateEmail(Long id, Users users) {
        users.setId(id);
        return emailRepository.save(users);
    }

    public boolean existsById(Long id) {
        return emailRepository.existsById(id);
    }

    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }
}
