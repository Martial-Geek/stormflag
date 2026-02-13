package com.stormflag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class NodeConfig {

    @Value("${storm.node-id}")
    private String nodeId;

    @Value("${storm.peers}")
    private String peersRaw;

    public String getNodeId() {
        return nodeId;
    }

    public List<String> getPeers() {
        return Arrays.asList(peersRaw.split(","));
    }
}
