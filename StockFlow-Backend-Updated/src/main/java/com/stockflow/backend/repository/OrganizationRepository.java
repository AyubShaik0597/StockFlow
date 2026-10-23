package com.stockflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockflow.backend.entity.Organization;

public interface OrganizationRepository
extends JpaRepository<Organization, Long> {
}
