package com.konicadigital.konica_app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shippingAddress")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shipping_address_id;

    private double longitude;
    private double latitude;

    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String street;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToMany(mappedBy = "shippingAddress", cascade = CascadeType.ALL)
    private List<Order> orders;

    public ShippingAddress(int shipping_address_id, double longitude, double latitude, String city, String street) {
        this.shipping_address_id = shipping_address_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.city = city;
        this.street = street;
    }

    public ShippingAddress() {

    }

    public int getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShipping_address_id(int shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
