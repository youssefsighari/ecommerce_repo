package Ecommerce.entities;

import jakarta.persistence.*;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // L'utilisateur auquel appartient le panier

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items; // Les produits dans le panier

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
