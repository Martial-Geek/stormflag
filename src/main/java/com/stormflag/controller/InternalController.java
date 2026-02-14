package com.stormflag.controller;

import com.stormflag.config.NodeConfig;
import org.springframework.web.bind.annotation.*;

import com.stormflag.raft.VoteRequest;
import com.stormflag.raft.VoteResponse;
import com.stormflag.raft.RaftNode;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final NodeConfig nodeConfig;
    private final RaftNode raftNode;

    public InternalController(NodeConfig nodeConfig, RaftNode raftNode) {
        this.nodeConfig = nodeConfig;
        this.raftNode = raftNode;
    }

    @PostMapping("/heartbeat")
    public void heartbeat(@RequestBody VoteRequest request) {
        raftNode.handleHeartbeat(request);
    }

    @PostMapping("/request-vote")
    public VoteResponse requestVote(@RequestBody VoteRequest request) {
        return raftNode.handleVoteRequest(request);
    }
}
