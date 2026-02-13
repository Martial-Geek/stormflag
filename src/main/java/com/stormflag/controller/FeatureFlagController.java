package com.stormflag.controller;

import com.stormflag.service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;
import com.stormflag.config.*;

import java.util.Map;


@RestController
@RequestMapping("/flags")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;
    private final NodeConfig nodeConfig;

    public FeatureFlagController(FeatureFlagService featureFlagService, NodeConfig nodeConfig) {
        this.featureFlagService = featureFlagService;
        this.nodeConfig = nodeConfig;
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

        return Map.of(
            "key", key,
            "enabled", enabled
        );
    }

    @GetMapping
    public Map<String, Boolean> getAllFlags() {
        return featureFlagService.getAllFlags();
    }

    @GetMapping("/node-info")
    public Map<String, Object> nodeInfo() {
        return Map.of(
            "nodeId", nodeConfig.getNodeId(),
            "peers", nodeConfig.getPeers()
    );
}

}
