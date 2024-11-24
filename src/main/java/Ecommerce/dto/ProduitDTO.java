package Ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProduitDTO {

    @NotNull(message = "Le nom du produit est obligatoire.")
    private String name;

    @Positive(message = "Le prix doit être supérieur à 0.")
    private double price;

    @NotNull(message = "L'ID de la catégorie est obligatoire.")
    private Long categorieId;

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }
}
