package com.stormflag.controller;

import org.springframework.web.bind.annotation.*;

import com.stormflag.raft.VoteRequest;
import com.stormflag.raft.VoteResponse;
import com.stormflag.raft.RaftNode;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final RaftNode raftNode;

    public InternalController(RaftNode raftNode) {
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
