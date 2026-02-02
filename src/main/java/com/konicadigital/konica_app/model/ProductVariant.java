package com.konicadigital.konica_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "product_variants")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int variant_id;

    private double height;
    private double width;
    private int quantity;
    private String design;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // Relation for Variant-level images
    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    private List<ProductImage> variantImages;

    public ProductVariant() {}

    public ProductVariant(double height, double width, int quantity, String design, Product product) {
        this.height = height;
        this.width = width;
        this.quantity = quantity;
        this.product = product;
        this.design = design;
    }

    // Getters and Setters
    public int getVariant_id() { return variant_id; }
    public void setVariant_id(int variant_id) { this.variant_id = variant_id; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public List<ProductImage> getVariantImages() { return variantImages; }
    public void setVariantImages(List<ProductImage> variantImages) { this.variantImages = variantImages; }

    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
}