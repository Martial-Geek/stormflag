package com.stormflag.controller;

import com.stormflag.service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @PostMapping
    public void createOrUpdateFlag(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("key");
        Boolean enabled = (Boolean) request.get("enabled");

        featureFlagService.setFlag(key, enabled);
    }

    @GetMapping("/{key}")
    public Map<String, Object> getFlag(@PathVariable String key) {
        Boolean enabled = featureFlagService.getFlag(key);

        if (enabled == null) {
            return Map.of(
                    "error", "Flag not found",
                    "key", key
            );
        }

        return Map.of(
                "key", key,
                "enabled", enabled
        );
    }

    @GetMapping
    public Map<String, Boolean> getAllFlags() {
        return featureFlagService.getAllFlags();
    }
}
