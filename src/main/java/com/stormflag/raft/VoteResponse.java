package com.stormflag.raft;

public class VoteResponse {

    private int term;
    private boolean voteGranted;

    public VoteResponse() {
    }

    public VoteResponse(int term, boolean voteGranted) {
        this.term = term;
        this.voteGranted = voteGranted;
    }

    public int getTerm() {
        return term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }
}
