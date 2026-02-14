package com.stormflag.service;

import com.stormflag.config.NodeConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalTime;

import java.util.List;

@Service
public class ClusterService {

    private final NodeConfig nodeConfig;
    private final RestTemplate restTemplate;

    public ClusterService(NodeConfig nodeConfig,
            RestTemplate restTemplate) {
        this.nodeConfig = nodeConfig;
        this.restTemplate = restTemplate;
    }

    public void pingPeers() {
        List<String> peers = nodeConfig.getPeers();

        for (String peer : peers) {
            try {
                String response = restTemplate.getForObject(
                        peer + "/internal/ping",
                        String.class);
                System.out.println("Ping response from " + peer + ": " + response);
            } catch (Exception e) {
                System.out.println("Failed to reach " + peer);
            }
        }
    }

    @Scheduled(fixedRate = 3000)
    public void heartbeat() {
        System.out.println("[" + nodeConfig.getNodeId() + "] Heartbeat at " + LocalTime.now());
        pingPeers();
    }
}
