package com.calendiary.calendiary_backend.security;

import com.calendiary.calendiary_backend.feign.AuthResponse;
import com.calendiary.calendiary_backend.feign.AuthServiceClient;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidator {

    private final AuthServiceClient authClient;

    @Cacheable(value = "tokenCache", key = "#bearerToken")
    public String validateAndGetUserId(String bearerToken) {
        AuthResponse response = authClient.validateToken(bearerToken);

        if (!response.isValid()) {
            throw new SecurityException("Invalid token");
        }
        return response.userId();
    }
}
