package Ecommerce.services;

import Ecommerce.entities.Cart;
import Ecommerce.entities.CartItem;
import Ecommerce.entities.Produit;
import Ecommerce.dto.CartDTO;
import Ecommerce.dto.CartItemDTO;
import Ecommerce.entities.AppUser;
import Ecommerce.repository.CartItemRepository;
import Ecommerce.repository.CartRepository;
import Ecommerce.repository.ProduitRepository;
import Ecommerce.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            newItem.setProduit(produit);
            newItem.setQuantity(quantity);

            // **Ajout de cette ligne : lier le CartItem au Cart**
            newItem.setCart(cart);

            cartItemRepository.save(newItem);
        }


        return cart;
    }

    // Afficher le contenu du panier
    public Cart getCartByUser(Long userId) {
        // Récupérer l'utilisateur par ID
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Charger le panier de l'utilisateur
        Cart cart = cartRepository.findByUser(user);

        // S'il n'y a pas de panier existant, le créer
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        return cart;
    }

    
    public CartDTO convertToDto(Cart cart) {
        // Créer une instance de CartDTO
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());

        // Conversion des items
        List<CartItemDTO> itemDTOs = cart.getItems().stream().map(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProduitName(item.getProduit().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getProduit().getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        cartDTO.setItems(itemDTOs);

        // Calcul du prix total
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getProduit().getPrice() * item.getQuantity())
                .sum();

        cartDTO.setTotalPrice(totalPrice); // Utilisation de l'instance

        return cartDTO;
    }



}
