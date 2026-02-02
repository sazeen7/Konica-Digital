package com.konicadigital.konica_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int image_id;

    private String image_name; // Main image path/url

    @Enumerated(EnumType.STRING)
    private ImageType type;

    private String design;

    // Link to Product (Nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    // Link to Variant (Nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private ProductVariant variant;

    public ProductImage() {}

    public ProductImage(String image_name, String design, ImageType type) {
        this.image_name = image_name;
        this.type = type;
        this.design = design;
    }

    // Getters and Setters
    public int getImage_id() { return image_id; }
    public void setImage_id(int image_id) { this.image_id = image_id; }

    public String getImage_name() { return image_name; }
    public void setImage_name(String image_name) { this.image_name = image_name; }

    public ImageType getType() { return type; }
    public void setType(ImageType type) { this.type = type; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }

    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
}