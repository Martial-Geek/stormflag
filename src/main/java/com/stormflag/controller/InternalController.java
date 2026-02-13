package com.stormflag.controller;

import com.stormflag.config.NodeConfig;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final NodeConfig nodeConfig;

    public InternalController(NodeConfig nodeConfig) {
        this.nodeConfig = nodeConfig;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "nodeId", nodeConfig.getNodeId(),
                "status", "alive"
        );
    }
}
