package com.stormflag.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureFlagService {

    private final ConcurrentHashMap<String, Boolean> flags = new ConcurrentHashMap<>();

    public void setFlag(String key, boolean enabled) {
        flags.put(key, enabled);
    }

    public Boolean getFlag(String key) {
        return flags.get(key);
    }

    public Map<String, Boolean> getAllFlags() {
        return flags;
    }
}
