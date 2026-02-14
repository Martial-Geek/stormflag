package com.stormflag.service;

import com.stormflag.config.NodeConfig;
import com.stormflag.raft.NodeState;
import com.stormflag.raft.RaftNode;
import com.stormflag.raft.VoteRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Service
public class ClusterService {

    private final NodeConfig nodeConfig;
    private final RestTemplate restTemplate;
    private final RaftNode raftNode;

    public ClusterService(NodeConfig nodeConfig,
            RestTemplate restTemplate,
            RaftNode raftNode) {
        this.nodeConfig = nodeConfig;
        this.restTemplate = restTemplate;
        this.raftNode = raftNode;
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

    @Scheduled(fixedRate = 2000)
    public void heartbeat() {

        if (raftNode.getState() != NodeState.LEADER) {
            return;
        }

        VoteRequest heartbeat = new VoteRequest(
                raftNode.getCurrentTerm(),
                nodeConfig.getNodeId());

        for (String peer : nodeConfig.getPeers()) {
            try {
                restTemplate.postForObject(
                        peer + "/internal/heartbeat",
                        heartbeat,
                        Void.class);
            } catch (Exception ignored) {
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void checkElection() {
        raftNode.checkElectionTimeout();
    }
}
