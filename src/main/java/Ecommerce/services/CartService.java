package Ecommerce.services;

import Ecommerce.entities.Cart;
import Ecommerce.entities.CartItem;
import Ecommerce.entities.Produit;
import Ecommerce.entities.AppUser;
import Ecommerce.repository.CartItemRepository;
import Ecommerce.repository.CartRepository;
import Ecommerce.repository.ProduitRepository;
import Ecommerce.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private AppUserRepository userRepository;

    // Ajouter un produit au panier
    public Cart addProductToCart(Long userId, Long produitId, int quantity) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit not found"));

        // Charger le panier de l'utilisateur, ou en créer un s'il n'existe pas
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // Vérifier si le produit existe déjà dans le panier
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduit().equals(produit))
                .findFirst();

        if (existingItem.isPresent()) {
            // Mettre à jour la quantité
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Ajouter un nouveau produit dans le panier
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduit(produit);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    // Afficher le contenu du panier
    public Cart getCartByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user);
    }
}
