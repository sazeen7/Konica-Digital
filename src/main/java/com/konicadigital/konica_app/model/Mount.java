package com.konicadigital.konica_app.model;

import jakarta.persistence.*;

@Entity
@Table(name= "mounts")
public class Mount {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int mount_id;

    private String mount_name;
    private byte[] mount_design;
    private double mount_width;
    private double mount_price;
    private int mount_stock;

    public Mount(int mount_id, String mount_name, byte[] mount_design, double mount_width, double mount_price, int mount_stock) {
        this.mount_id = mount_id;
        this.mount_name = mount_name;
        this.mount_design = mount_design;
        this.mount_width = mount_width;
        this.mount_price = mount_price;
        this.mount_stock = mount_stock;
    }

    public Mount() {

    }

    public int getMount_id() {
        return mount_id;
    }

    public void setMount_id(int mount_id) {
        this.mount_id = mount_id;
    }

    public String getMount_name() {
        return mount_name;
    }

    public void setMount_name(String mount_name) {
        this.mount_name = mount_name;
    }

    public byte[] getMount_design() {
        return mount_design;
    }

    public void setMount_design(byte[] mount_design) {
        this.mount_design = mount_design;
    }

    public double getMount_width() {
        return mount_width;
    }

    public void setMount_width(double mount_width) {
        this.mount_width = mount_width;
    }

    public double getMount_price() {
        return mount_price;
    }

    public void setMount_price(double mount_price) {
        this.mount_price = mount_price;
    }

    public int getMount_stock() {
        return mount_stock;
    }

    public void setMount_stock(int mount_stock) {
        this.mount_stock = mount_stock;
    }
}
