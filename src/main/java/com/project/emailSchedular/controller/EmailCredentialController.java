package com.project.emailSchedular.controller;

import com.project.emailSchedular.entity.EmailCredential;
import com.project.emailSchedular.repository.EmailCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email-credentials")
public class EmailCredentialController {
    @Autowired
    private EmailCredentialRepository emailCredentialRepository;

    @GetMapping
    public List<EmailCredential> getAllCredentials() {
        return emailCredentialRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailCredential> getCredentialById(@PathVariable Long id) {
        return emailCredentialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmailCredential createCredential(@RequestBody EmailCredential credential) {
        return emailCredentialRepository.save(credential);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailCredential> updateCredential(@PathVariable Long id, @RequestBody EmailCredential credential) {
        if (!emailCredentialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        credential.setId(id);
        return ResponseEntity.ok(emailCredentialRepository.save(credential));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        if (!emailCredentialRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        emailCredentialRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
