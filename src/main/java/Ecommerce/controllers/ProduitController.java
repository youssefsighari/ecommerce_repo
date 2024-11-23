package Ecommerce.controllers;

import Ecommerce.entities.Produit;
import Ecommerce.services.ProduitService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/produits")
public class ProduitController {

    private final ProduitService produitService;

    @Autowired
    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        return ResponseEntity.ok(produitService.createProduit(produit));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produitDetails) {
        return ResponseEntity.ok(produitService.updateProduit(id, produitDetails));
    } 

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.ok("Produit with ID: " + id + " deleted successfully.");
    }
    
    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllProduits(HttpServletRequest request) {
    	System.out.println("HTTP method received: " + request.getMethod());
        System.out.println("User attempting to delete all produits.");
        produitService.deleteAllProduits();
        return ResponseEntity.ok("Tous les produits ont été supprimés avec succès.");
    }

}
