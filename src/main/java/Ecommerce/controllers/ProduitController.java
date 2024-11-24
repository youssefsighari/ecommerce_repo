package Ecommerce.controllers;

import Ecommerce.dto.ProduitDTO;
import Ecommerce.dto.ProduitResponseDTO;
import Ecommerce.entities.Categorie;
import Ecommerce.entities.Produit;
import Ecommerce.repository.CategorieRepository;
import Ecommerce.repository.ProduitRepository;

import Ecommerce.services.ProduitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/produits")
public class ProduitController {

    private final ProduitService produitService;
    
    private final CategorieRepository categorieRepository;
    
    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitController(ProduitService produitService, CategorieRepository categorieRepository, ProduitRepository produitRepository) {
        this.produitService = produitService;
        this.categorieRepository=categorieRepository;
        this.produitRepository=produitRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitResponseDTO> createProduit(@Valid @RequestBody ProduitDTO produitDTO) {
        Produit produit = produitService.createProduit(produitDTO);

        // Construire le DTO de réponse
        ProduitResponseDTO responseDTO = new ProduitResponseDTO();
        responseDTO.setId(produit.getId());
        responseDTO.setName(produit.getName());
        responseDTO.setPrice(produit.getPrice());
        responseDTO.setCategorieName(produit.getCategorie().getName()); // Nom de la catégorie

        return ResponseEntity.ok(responseDTO);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        Produit updatedProduit = produitService.updateProduit(id, produitDTO);

        // Préparer la réponse
        ProduitDTO responseDTO = new ProduitDTO();
        responseDTO.setName(updatedProduit.getName());
        responseDTO.setPrice(updatedProduit.getPrice());
        responseDTO.setCategorieId(updatedProduit.getCategorie().getId());

        return ResponseEntity.ok(responseDTO);
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
