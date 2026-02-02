package com.konicadigital.konica_app.repository;

import com.konicadigital.konica_app.model.Price;
import com.konicadigital.konica_app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
    Optional<Price> findByVariant(ProductVariant variant);
}
