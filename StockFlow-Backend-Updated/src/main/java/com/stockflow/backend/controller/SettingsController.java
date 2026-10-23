package com.stockflow.backend.controller;


import com.stockflow.backend.dto.SettingsRequest;
import com.stockflow.backend.dto.SettingsResponse;
import com.stockflow.backend.service.SettingsService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<SettingsResponse> getSettings() {
        return ResponseEntity.ok(settingsService.getSettings());
    }

    @PutMapping
    public ResponseEntity<SettingsResponse> updateSettings(
            @Valid @RequestBody SettingsRequest request) {
        return ResponseEntity.ok(
                settingsService.updateSettings(request));
    }
}
