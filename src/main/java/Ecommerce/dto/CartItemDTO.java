package Ecommerce.dto;


public class CartItemDTO {
    private Long id;
    private String produitName;
    private int quantity;
    private double price;

    // Getters et Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProduitName() {
        return produitName;
    }
    public void setProduitName(String produitName) {
        this.produitName = produitName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
