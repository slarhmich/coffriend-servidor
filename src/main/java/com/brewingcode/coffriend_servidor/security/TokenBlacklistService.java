package com.brewingcode.coffriend_servidor.security;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage token blacklist for logout functionality.
 * Stores invalidated tokens to prevent their use after logout.
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Invalidate a token
     * @param token the JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Check if a token is blacklisted
     * @param token the JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Clear all blacklisted tokens
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}
