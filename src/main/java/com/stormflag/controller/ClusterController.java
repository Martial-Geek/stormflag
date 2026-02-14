package com.stormflag.controller;

import com.stormflag.config.NodeConfig;
import com.stormflag.service.ClusterService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    private final ClusterService clusterService;
    private final NodeConfig nodeConfig;

    public ClusterController(ClusterService clusterService,
            NodeConfig nodeConfig) {
        this.clusterService = clusterService;
        this.nodeConfig = nodeConfig;
    }

    @GetMapping("/ping")
    public String pingPeers() {
        clusterService.pingPeers();
        return "Pinged peers. Check logs.";
    }

    @GetMapping("/node-info")
    public Map<String, Object> nodeInfo() {
        return Map.of(
                "nodeId", nodeConfig.getNodeId(),
                "peers", nodeConfig.getPeers());
    }
}
