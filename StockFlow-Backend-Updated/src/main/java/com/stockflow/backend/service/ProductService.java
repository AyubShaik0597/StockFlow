package com.stockflow.backend.service;


import com.stockflow.backend.dto.ProductRequest;
import com.stockflow.backend.dto.ProductResponse;
import com.stockflow.backend.entity.Product;
import com.stockflow.backend.entity.User;
import com.stockflow.backend.repository.ProductRepository;
import com.stockflow.backend.repository.UserRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Please log in");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Product getProductForOrganization(Long id, Long organizationId) {
        return productRepository
                .findByIdAndOrganization_Id(id, organizationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));
    }

    public ProductResponse createProduct(ProductRequest request) {
        User user = getLoggedInUser();
        Long organizationId = user.getOrganization().getId();

        String sku = request.getSku().trim();

        if (productRepository.existsByOrganization_IdAndSkuIgnoreCase(
                organizationId, sku)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A product with this SKU already exists in your organization");
        }

        Product product = new Product();
        product.setOrganization(user.getOrganization());
        applyRequest(product, request);
        product.setSku(sku);

        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String search) {
        User user = getLoggedInUser();
        Long organizationId = user.getOrganization().getId();

        List<Product> products;

        if (search == null || search.isBlank()) {
            products = productRepository
                    .findAllByOrganization_IdOrderByCreatedAtDesc(
                            organizationId);
        } else {
            String term = search.trim();

            products = productRepository
                    .findByOrganization_IdAndNameContainingIgnoreCaseOrOrganization_IdAndSkuContainingIgnoreCase(
                            organizationId, term, organizationId, term);
        }

        return products.stream()
                .map(ProductResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        User user = getLoggedInUser();
        Long organizationId = user.getOrganization().getId();

        Product product = getProductForOrganization(id, organizationId);
        return new ProductResponse(product);
    }

    public ProductResponse updateProduct(
            Long id, ProductRequest request) {

        User user = getLoggedInUser();
        Long organizationId = user.getOrganization().getId();

        Product product = getProductForOrganization(id, organizationId);
        String sku = request.getSku().trim();

        productRepository
                .findByOrganization_IdAndSkuIgnoreCase(organizationId, sku)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "A product with this SKU already exists in your organization");
                    }
                });

        applyRequest(product, request);
        product.setSku(sku);

        Product updatedProduct = productRepository.save(product);
        return new ProductResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        User user = getLoggedInUser();
        Long organizationId = user.getOrganization().getId();

        Product product = getProductForOrganization(id, organizationId);
        productRepository.delete(product);
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName().trim());
        product.setSku(request.getSku().trim());
        product.setDescription(request.getDescription());
        product.setQuantityOnHand(request.getQuantityOnHand());
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setLowStockThreshold(request.getLowStockThreshold());
    }
}
