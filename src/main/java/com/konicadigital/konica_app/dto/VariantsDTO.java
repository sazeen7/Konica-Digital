package com.konicadigital.konica_app.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class VariantsDTO {
    private double width;
    private double height;
    private int quantity;
    private double price;
    private List<MultipartFile> variantImages;
    private String design;

    public VariantsDTO() {}

    // Getters and Setters
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public List<MultipartFile> getVariantImages() { return variantImages; }
    public void setVariantImages(List<MultipartFile> variantImages) { this.variantImages = variantImages; }

    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
}