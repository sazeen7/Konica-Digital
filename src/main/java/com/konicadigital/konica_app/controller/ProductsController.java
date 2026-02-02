package com.konicadigital.konica_app.controller;

import com.konicadigital.konica_app.dto.ProductsDTO;
import com.konicadigital.konica_app.dto.VariantsDTO;
import com.konicadigital.konica_app.model.Product;
import com.konicadigital.konica_app.model.ProductVariant;
import com.konicadigital.konica_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@ModelAttribute ProductsDTO dto) {
        try {
            Product createdProduct = productService.addProductWithVariant(dto);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT http://localhost:8080/api/products/1
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @ModelAttribute ProductsDTO dto) {
        try {
            Product updatedProduct = productService.updateProduct(id, dto);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/variants")
    public ResponseEntity<ProductVariant> addVariant(
            @PathVariable int id,
            @ModelAttribute VariantsDTO dto) {
        try {
            ProductVariant newVariant = productService.addNewVariant(id, dto);
            return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // GET http://localhost:8080/api/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}