package Ecommerce.controllers;

import Ecommerce.entities.Categorie;
import Ecommerce.services.CategorieService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
public class CategorieController {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie) {
        return ResponseEntity.ok(categorieService.createCategorie(categorie));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @RequestBody Categorie categorieDetails) {
        return ResponseEntity.ok(categorieService.updateCategorie(id, categorieDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.ok("Categorie with ID: " + id + " deleted successfully.");
    }
    
    
    
    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllCategories(HttpServletRequest request) {
    	System.out.println("HTTP method received: " + request.getMethod());
        System.out.println("User attempting to delete all categories.");
        categorieService.deleteAllCategories();
        return ResponseEntity.ok("Tous les categories ont été supprimés avec succès.");
    }
} 
