package com.stormflag.raft;

import com.stormflag.config.NodeConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RaftNode {

    private final NodeConfig nodeConfig;
    private final RestTemplate restTemplate;
    private final AtomicReference<NodeState> state = new AtomicReference<>(NodeState.FOLLOWER);
    private final Random random = new Random();

    private long lastHeartbeatTime = System.currentTimeMillis();
    private long electionTimeout;

    private int currentTerm = 0;
    private String votedFor = null;

    public RaftNode(NodeConfig nodeConfig, RestTemplate restTemplate) {
        this.nodeConfig = nodeConfig;
        this.restTemplate = restTemplate;
        resetElectionTimeout();
    }

    public int getCurrentTerm() {
        return currentTerm;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public NodeState getState() {
        return state.get();
    }

    public void resetHeartbeat() {
        lastHeartbeatTime = System.currentTimeMillis();
    }

    private void resetElectionTimeout() {
        electionTimeout = 3000 + random.nextInt(3000); // 3–6 seconds
    }

    public synchronized void checkElectionTimeout() {

        if (state.get() != NodeState.FOLLOWER) {
            return;
        }

        long now = System.currentTimeMillis();

        if (now - lastHeartbeatTime > electionTimeout) {

            currentTerm++;
            state.set(NodeState.CANDIDATE);
            votedFor = nodeConfig.getNodeId();

            System.out.println("[" + nodeConfig.getNodeId() +
                    "] Becoming CANDIDATE for term " + currentTerm);

            resetElectionTimeout();
            lastHeartbeatTime = now;

            requestVotes();
        }
    }

    public synchronized VoteResponse handleVoteRequest(VoteRequest request) {

        if (request.getTerm() < currentTerm) {
            return new VoteResponse(currentTerm, false);
        }

        if (request.getTerm() > currentTerm) {
            currentTerm = request.getTerm();
            state.set(NodeState.FOLLOWER);
            votedFor = null;
        }

        if (votedFor == null || votedFor.equals(request.getCandidateId())) {
            votedFor = request.getCandidateId();
            resetHeartbeat();
            return new VoteResponse(currentTerm, true);
        }

        return new VoteResponse(currentTerm, false);
    }

    private void requestVotes() {
        int votes = 1; // vote for self
        int totalNodes = nodeConfig.getPeers().size() + 1;

        VoteRequest request = new VoteRequest(currentTerm, nodeConfig.getNodeId());

        for (String peer : nodeConfig.getPeers()) {
            try {
                VoteResponse response = restTemplate.postForObject(
                        peer + "/internal/request-vote",
                        request,
                        VoteResponse.class);

                if (response != null && response.isVoteGranted()) {
                    votes++;
                }

            } catch (Exception e) {
                // peer unreachable
            }
        }

        if (votes > totalNodes / 2) {
            state.set(NodeState.LEADER);
            System.out.println("[" + nodeConfig.getNodeId() +
                    "] Became LEADER for term " + currentTerm);

            resetHeartbeat(); // very important
        }
    }

    public synchronized void handleHeartbeat(VoteRequest request) {

        if (request.getTerm() >= currentTerm) {
            currentTerm = request.getTerm();
            state.set(NodeState.FOLLOWER);
            votedFor = null;
            resetHeartbeat();
        }
    }
}
