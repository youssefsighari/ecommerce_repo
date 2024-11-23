package Ecommerce.services;

import Ecommerce.entities.Produit;
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

    public Produit createProduit(Produit produit) {
        Categorie categorie = categorieRepository.findById(produit.getCategorie().getId())
                .orElseThrow(() -> new RuntimeException("Categorie not found with id " + produit.getCategorie().getId()));

        produit.setCategorie(categorie);
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Long id, Produit produitDetails) {
        Produit produit = getProduitById(id);

        produit.setName(produitDetails.getName());
        produit.setPrice(produitDetails.getPrice());

        Categorie categorie = categorieRepository.findById(produitDetails.getCategorie().getId())
                .orElseThrow(() -> new RuntimeException("Categorie not found with id " + produitDetails.getCategorie().getId()));
        produit.setCategorie(categorie);

        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Produit not found with ID: " + id);
        }
        produitRepository.deleteById(id);
    }
    
    public void deleteAllProduits() {
        produitRepository.deleteAll();
    }
}
