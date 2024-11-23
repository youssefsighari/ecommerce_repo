package Ecommerce.controllers;

import Ecommerce.entities.Cart;
import Ecommerce.services.CartService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{produitId}")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long produitId,
            @RequestBody Map<String, Integer> body) {
        int quantity = body.get("quantity"); // Récupérer "quantity" du corps JSON
        return ResponseEntity.ok(cartService.addProductToCart(userId, produitId, quantity));
    }
    
    


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
   
    public ResponseEntity<Cart> getCartByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }
}
