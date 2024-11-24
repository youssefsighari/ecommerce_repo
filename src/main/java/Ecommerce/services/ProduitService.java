package Ecommerce.services;

import Ecommerce.entities.Produit;
import Ecommerce.dto.ProduitDTO;
import Ecommerce.entities.Categorie;
import Ecommerce.repository.ProduitRepository;
import Ecommerce.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository, CategorieRepository categorieRepository) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with id " + id));
    }

    public Produit createProduit(ProduitDTO produitDTO) {
        // Vérifiez si l'ID de la catégorie est défini
        if (produitDTO.getCategorieId() == null) {
            throw new RuntimeException("La catégorie est obligatoire et doit contenir un ID valide.");
        }

        // Vérifiez si la catégorie existe dans la base de données
        Categorie categorie = categorieRepository.findById(produitDTO.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Categorie not found with ID: " + produitDTO.getCategorieId()));

        // Créez un nouvel objet Produit à partir du DTO
        Produit produit = new Produit();
        produit.setName(produitDTO.getName());
        produit.setPrice(produitDTO.getPrice());
        produit.setCategorie(categorie);

        // Sauvegardez le produit dans le dépôt
        return produitRepository.save(produit);
    }




    public Produit updateProduit(Long id, ProduitDTO produitDTO) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with ID: " + id));

        // Mettre à jour les champs
        produit.setName(produitDTO.getName());
        produit.setPrice(produitDTO.getPrice());

        // Vérifier si la catégorie existe
        Categorie categorie = categorieRepository.findById(produitDTO.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Categorie not found with ID: " + produitDTO.getCategorieId()));
        produit.setCategorie(categorie);

        return produitRepository.save(produit);
    }


    public void deleteProduit(Long id) {
        Produit produit = getProduitById(id);

        // Supprimer les relations avec CartItem (si cascade non activée)
        produit.getCartItems().clear();

        produitRepository.delete(produit);
    }
    
    public void deleteAllProduits() {
        produitRepository.deleteAll();
    }
}
