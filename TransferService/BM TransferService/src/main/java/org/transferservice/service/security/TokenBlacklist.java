package org.transferservice.service.security;

public interface TokenBlacklist {

    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
