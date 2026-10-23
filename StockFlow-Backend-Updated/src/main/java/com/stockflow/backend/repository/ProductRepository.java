package com.stockflow.backend.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stockflow.backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("""
		    SELECT COUNT(p)
		    FROM Product p
		    WHERE p.organization.id = :organizationId
		    """)
		long countProductsByOrganization(
		        @Param("organizationId") Long organizationId);

		@Query("""
		    SELECT COALESCE(SUM(p.quantityOnHand), 0)
		    FROM Product p
		    WHERE p.organization.id = :organizationId
		    """)
		long sumQuantityByOrganization(
		        @Param("organizationId") Long organizationId);

		@Query("""
		    SELECT p
		    FROM Product p
		    WHERE p.organization.id = :organizationId
		      AND p.quantityOnHand <=
		          COALESCE(p.lowStockThreshold,
		                   p.organization.defaultLowStockThreshold)
		    ORDER BY p.quantityOnHand ASC
		    """)
		List<Product> findLowStockProducts(
		        @Param("organizationId") Long organizationId);

    List<Product> findAllByOrganization_IdOrderByCreatedAtDesc(
            Long organizationId);

    Optional<Product> findByIdAndOrganization_Id(
            Long id, Long organizationId);

    boolean existsByOrganization_IdAndSkuIgnoreCase(
            Long organizationId, String sku);

    Optional<Product> findByOrganization_IdAndSkuIgnoreCase(
            Long organizationId, String sku);

    List<Product>
    findByOrganization_IdAndNameContainingIgnoreCaseOrOrganization_IdAndSkuContainingIgnoreCase(
            Long organizationId1, String name,
            Long organizationId2, String sku);
}
