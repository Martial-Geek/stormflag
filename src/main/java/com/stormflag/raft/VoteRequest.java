package com.stormflag.raft;

public class VoteRequest {

    private int term;
    private String candidateId;

    public VoteRequest() {
    }

    public VoteRequest(int term, String candidateId) {
        this.term = term;
        this.candidateId = candidateId;
    }

    public int getTerm() {
        return term;
    }

    public String getCandidateId() {
        return candidateId;
    }
}
