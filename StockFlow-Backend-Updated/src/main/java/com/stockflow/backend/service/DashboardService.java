package com.stockflow.backend.service;


import com.stockflow.backend.dto.DashboardResponse;
import com.stockflow.backend.dto.ProductResponse;

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
public class DashboardService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DashboardService(
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Please log in");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));

        Long organizationId = user.getOrganization().getId();

        long totalProducts =
                productRepository.countProductsByOrganization(organizationId);

        long totalQuantity =
                productRepository.sumQuantityByOrganization(organizationId);

        List<ProductResponse> lowStockProducts =
                productRepository.findLowStockProducts(organizationId)
                        .stream()
                        .map(ProductResponse::new)
                        .toList();

        return new DashboardResponse(
                totalProducts, totalQuantity, lowStockProducts);
    }
}
