package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/provisioning")
public class ProvisioningController {
    private final ProvisioningService provisioningService;

    @GetMapping("/{macAddress}")
    public ResponseEntity<String> getDivaceConfiguration(@PathVariable String macAddress) {
        String configuration = provisioningService.getProvisioningFile(macAddress);
        return ResponseEntity.ok(configuration);
    }
}
