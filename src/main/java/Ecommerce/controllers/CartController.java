package Ecommerce.controllers;

import Ecommerce.configurations.ResourceNotFoundException; 
import Ecommerce.dto.CartDTO;
import Ecommerce.entities.Cart;
import Ecommerce.repository.CartRepository;
import Ecommerce.services.CartService;

import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository carterepository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userId}/add/{produitId}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long produitId,
            @RequestBody Map<String, Integer> body) {
        int quantity = body.get("quantity");
        Cart cart = cartService.addProductToCart(userId, produitId, quantity);

        // Convertir en DTO
        CartDTO cartDTO = cartService.convertToDto(cart);
        return ResponseEntity.ok(cartDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable Long userId) {
        // Récupérer le panier de l'utilisateur à partir du service
        Cart cart = cartService.getCartByUser(userId);

        // Convertir l'entité Cart en DTO
        CartDTO cartDTO = cartService.convertToDto(cart);

        // Retourner la réponse
        return ResponseEntity.ok(cartDTO);
    }


}
