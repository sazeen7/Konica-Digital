package com.konicadigital.konica_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customizations")
public class Customize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customize_id;

    private boolean useMount;
    private boolean useGlass;

    // Assuming customizations are specific to a Variant (e.g., a specific size allows mounting)
    // If it applies to the whole Product, change this to Product
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    public Customize() {}

    public Customize(boolean useMount, boolean useGlass) {
        this.useMount = useMount;
        this.useGlass = useGlass;
    }

    // Getters and Setters
    public int getCustomize_id() { return customize_id; }
    public void setCustomize_id(int customize_id) { this.customize_id = customize_id; }

    public boolean isUseMount() { return useMount; }
    public void setUseMount(boolean useMount) { this.useMount = useMount; }

    public boolean isUseGlass() { return useGlass; }
    public void setUseGlass(boolean useGlass) { this.useGlass = useGlass; }

    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }
}