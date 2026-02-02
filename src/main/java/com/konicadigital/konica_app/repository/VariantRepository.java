package com.konicadigital.konica_app.repository;

import com.konicadigital.konica_app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<ProductVariant, Integer> {}
