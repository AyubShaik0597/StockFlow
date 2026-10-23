package com.stockflow.backend.service;

import com.stockflow.backend.dto.SettingsRequest;
import com.stockflow.backend.dto.SettingsResponse;
import com.stockflow.backend.entity.Organization;
import com.stockflow.backend.entity.User;
import com.stockflow.backend.repository.OrganizationRepository;
import com.stockflow.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SettingsService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public SettingsService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    private Organization getCurrentOrganization() {
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

        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Organization not found"));
    }

    @Transactional(readOnly = true)
    public SettingsResponse getSettings() {
        Organization organization = getCurrentOrganization();

        return new SettingsResponse(
                organization.getName(),
                organization.getDefaultLowStockThreshold());
    }

    public SettingsResponse updateSettings(SettingsRequest request) {
        Organization organization = getCurrentOrganization();

        organization.setDefaultLowStockThreshold(
                request.getDefaultLowStockThreshold());

        Organization savedOrganization =
                organizationRepository.save(organization);

        return new SettingsResponse(
                savedOrganization.getName(),
                savedOrganization.getDefaultLowStockThreshold());
    }
}
