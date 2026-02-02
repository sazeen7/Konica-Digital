package com.konicadigital.konica_app.repository;

import com.konicadigital.konica_app.model.ProductImage;
import com.konicadigital.konica_app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    // Find all images linked to a specific variant
    List<ProductImage> findByVariant(ProductVariant variant);
}