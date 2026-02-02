package com.konicadigital.konica_app.service;

import com.konicadigital.konica_app.dto.ProductsDTO;
import com.konicadigital.konica_app.dto.VariantsDTO;
import com.konicadigital.konica_app.model.*;
import com.konicadigital.konica_app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private VariantRepository variantRepo;
    @Autowired private PriceRepository priceRepo;
    @Autowired private ProductImageRepository imageRepo;

    @Transactional
    public Product addProductWithVariant(ProductsDTO dto) throws IOException {

        // 1. Create & Save Parent Product
        Product product = new Product();
        product.setProduct_name(dto.getName());
        product.setDescription(dto.getDescription());

        // Fix: Actual Category Logic
        Category category = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getCategory()));

        // Clear and add ensures we don't have null pointer issues if the set is empty
        product.setCategories(new HashSet<>());
        product.getCategories().add(category);

        product = productRepo.save(product);

        // 2. Determine Design Name (Fix for null design)
        // If user provides a design (e.g., "Matte Black"), use it. Otherwise, default to "Standard".
        String designName = (dto.getDesign() != null && !dto.getDesign().isEmpty()) ? dto.getDesign() : "Standard";

        // 3. Create & Save First Variant
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setWidth(Math.min(dto.getWidth(), dto.getHeight()));
        variant.setHeight(Math.max(dto.getWidth(), dto.getHeight()));
        variant.setQuantity(dto.getQuantity());
        variant.setDesign(designName); // <--- Save the Design Name here

        variant = variantRepo.save(variant);

        // 4. Create Price
        Price price = new Price();
        price.setPrice(dto.getPrice());
        price.setVariant(variant);
        priceRepo.save(price);

        // --- RESPONSE FIX: Initialize lists so JSON is not empty ---
        List<ProductVariant> variantsList = new ArrayList<>();
        variantsList.add(variant);
        product.setVariants(variantsList);

        List<ProductImage> imagesList = new ArrayList<>();

        // 5. Handle Images

        // A. Generic Product Image (Design is null)
        if (dto.getProductImage() != null && !dto.getProductImage().isEmpty()) {
            ProductImage img = saveImage(dto.getProductImage(), ImageType.PRODUCT, product, null, null);
            imagesList.add(img);
        }

        // B. Specific Variant Image (Uses the designName we determined above)
        if (dto.getVariantImage() != null && !dto.getVariantImage().isEmpty()) {
            // Fix: Pass 'designName' variable instead of hardcoded "Default"
            ProductImage img = saveImage(dto.getVariantImage(), ImageType.VARIANT, product, variant, designName);
            imagesList.add(img);
        }

        // Attach images to product for the response
        product.setProductImages(imagesList);

        return product;
    }

    // Helper for saving images
    private ProductImage saveImage(MultipartFile file, ImageType type, Product product, ProductVariant variant, String designName) throws IOException {
        String uploadDir = "public/images/";
        String uniqueName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir, uniqueName);
        if (!Files.exists(path.getParent())) Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        ProductImage image = new ProductImage();
        image.setImage_name(uniqueName);
        image.setType(type);
        image.setProduct(product);

        image.setDesign(designName);

        if (type == ImageType.VARIANT) {
            image.setVariant(variant);
        }

        return imageRepo.save(image);
    }

    @Transactional
    public ProductVariant addNewVariant(int productId, VariantsDTO dto) throws IOException {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 1. Create Variant with Design Name
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setWidth(Math.min(dto.getWidth(), dto.getHeight()));
        variant.setHeight(Math.max(dto.getWidth(), dto.getHeight()));
        variant.setQuantity(dto.getQuantity());
        variant.setDesign(dto.getDesign()); // <--- Save the design name (e.g., "Matte Black")

        variant = variantRepo.save(variant);

        // 2. Save Price
        Price price = new Price();
        price.setPrice(dto.getPrice());
        price.setVariant(variant);
        priceRepo.save(price);

        // 3. Save Images (Tag them with the Design Name)
        if (dto.getVariantImages() != null && !dto.getVariantImages().isEmpty()) {
            for (MultipartFile file : dto.getVariantImages()) {
                saveImage(file, ImageType.VARIANT, product, variant, dto.getDesign());
            }
        }

        return variant;
    }

    @Transactional
    public Product updateProduct(int productId, ProductsDTO dto) throws IOException {
        // 1. Fetch Existing Product
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // 2. Update Product Fields
        product.setProduct_name(dto.getName());
        product.setDescription(dto.getDescription());

        // Update Category if changed
        Category category = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.getCategories().clear(); // Remove old categories
        product.getCategories().add(category); // Add new one

        product = productRepo.save(product);

        // 3. Update Existing Variant
        // (For now, we assume we are updating the first/primary variant found)
        List<ProductVariant> variants = product.getVariants();
        ProductVariant variant;

        if (variants != null && !variants.isEmpty()) {
            variant = variants.get(0); // Get the first one
        } else {
            // Safety: If no variant exists, create a new one (Corner case)
            variant = new ProductVariant();
            variant.setProduct(product);
        }

        variant.setWidth(Math.min(dto.getWidth(), dto.getHeight()));
        variant.setHeight(Math.max(dto.getWidth(), dto.getHeight()));
        variant.setQuantity(dto.getQuantity());

        if (dto.getDesign() != null && !dto.getDesign().isEmpty()) {
            variant.setDesign(dto.getDesign());
        }

        variant = variantRepo.save(variant);

        // 4. Update Price
        // We look for the price linked to this specific variant
        Price priceEntity = priceRepo.findByVariant(variant)
                .orElse(new Price()); // If no price exists, create new

        priceEntity.setPrice(dto.getPrice());
        priceEntity.setVariant(variant);
        priceRepo.save(priceEntity);

        // 5. Handle Image Updates
        // Only update if the user actually uploaded a new file.

        // A. Update Main Product Image
        if (dto.getProductImage() != null && !dto.getProductImage().isEmpty()) {
            // FIX: Add 'null' at the end because main product images don't have a specific design
            saveImage(dto.getProductImage(), ImageType.PRODUCT, product, null, null);
        }

        // B. Update Variant Image
        if (dto.getVariantImage() != null && !dto.getVariantImage().isEmpty()) {
            // FIX: Add "Default" (or null) as the 5th argument
            saveImage(dto.getVariantImage(), ImageType.VARIANT, product, variant, "Default");
        }

        return product;
    }

    public Product getProductById(int id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        // --- FIX: Filter out Variant images from the main list ---
        // We only want images where type is "PRODUCT" to show in the main gallery
        List<ProductImage> mainGalleryImages = product.getProductImages().stream()
                .filter(img -> img.getType() == ImageType.PRODUCT)
                .collect(Collectors.toList());

        // Update the product object in memory (this doesn't delete from DB)
        product.setProductImages(mainGalleryImages);
        // ---------------------------------------------------------

        return product;
    }
}