package com.konicadigital.konica_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double cart_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    public Cart(double cart_id) {
        this.cart_id = cart_id;
    }

    public Cart() {

    }

    public double getCart_id() {
        return cart_id;
    }

    public void setCart_id(double cart_id) {
        this.cart_id = cart_id;
    }
}
