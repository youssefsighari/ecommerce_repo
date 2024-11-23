package Ecommerce.services;

import Ecommerce.entities.Categorie;
import Ecommerce.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found with id " + id));
    }

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorie(Long id, Categorie categorieDetails) {
        Categorie categorie = getCategorieById(id);
        categorie.setName(categorieDetails.getName());
        return categorieRepository.save(categorie);
    }

    public void deleteCategorie(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Categorie not found with ID: " + id);
        }
        categorieRepository.deleteById(id);
    }
    
    public void deleteAllCategories() {
    	categorieRepository.deleteAll();
    }
}
