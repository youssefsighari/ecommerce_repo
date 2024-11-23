package Ecommerce.controllers;

import Ecommerce.entities.AppUser;
import Ecommerce.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AdminController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return ResponseEntity.ok(users);
    }

    
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }
 
    
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!appUserRepository.existsById(id)) {
            return ResponseEntity.status(404).body("User not found with ID: " + id);
        }

        appUserRepository.deleteById(id);
        return ResponseEntity.ok("User with ID: " + id + " deleted successfully.");
    }
}
