package com.project.emailSchedular.controller;

import com.project.emailSchedular.entity.Users;
import com.project.emailSchedular.service.EmailCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailCrudController {
    @Autowired
    private EmailCrudService emailCrudService;

    @GetMapping
    public List<Users> getAllEmails() {
        return emailCrudService.getAllEmails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getEmailById(@PathVariable Long id) {
        return emailCrudService.getEmailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Users createEmail(@RequestBody Users users) {
        return emailCrudService.createEmail(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateEmail(@PathVariable Long id, @RequestBody Users users) {
        if (!emailCrudService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        users.setId(id);
        return ResponseEntity.ok(emailCrudService.updateEmail(id, users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        if (!emailCrudService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        emailCrudService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Users> patchEmail(@PathVariable Long id, @RequestBody Users users) {
        return emailCrudService.getEmailById(id)
                .map(existing -> {
                    // Only update non-null fields
                    if (users.getName() != null) existing.setName(users.getName());
                    if (users.getEmailDate() != null) existing.setEmailDate(users.getEmailDate());
                    if (users.getSubject() != null) existing.setSubject(users.getSubject());
                    if (users.getContent() != null) existing.setContent(users.getContent());
                    if (users.getEmail() != null) existing.setEmail(users.getEmail());
                    if (users.getCc() != null) existing.setCc(users.getCc());
                    if (users.getBcc() != null) existing.setBcc(users.getBcc());
                    if (users.getTemplate() != null) existing.setTemplate(users.getTemplate());
                    if (users.getStatus() != null) existing.setStatus(users.getStatus());
                    Users updated = emailCrudService.updateEmail(id, existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
