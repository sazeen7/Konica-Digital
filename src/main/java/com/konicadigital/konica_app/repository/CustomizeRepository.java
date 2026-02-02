package com.konicadigital.konica_app.repository;

import com.konicadigital.konica_app.model.Customize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizeRepository extends JpaRepository<Customize, Integer> {}
