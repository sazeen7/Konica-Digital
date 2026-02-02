package com.konicadigital.konica_app.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProductsDTO {

    // --- PRODUCT FIELDS (Parent) ---
    private String name;
    private String description;
    private int category;
    private MultipartFile productImage; // Generic image for the product catalog

    // --- VARIANT FIELDS (Child) ---
    private double width;
    private double height;
    private int quantity;
    private String design;

    // --- PRICE FIELDS ---
    private double price;

    // --- VARIANT SPECIFIC IMAGE ---
    private MultipartFile variantImage; // Specific image for this size/variant

    public ProductsDTO() {}

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }

    public MultipartFile getProductImage() { return productImage; }
    public void setProductImage(MultipartFile productImage) { this.productImage = productImage; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public MultipartFile getVariantImage() { return variantImage; }
    public void setVariantImage(MultipartFile variantImage) { this.variantImage = variantImage; }

    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
}