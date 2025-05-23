package com.calendiary.calendiary_backend.security;

import com.calendiary.calendiary_backend.feign.AuthResponse;
import com.calendiary.calendiary_backend.feign.AuthServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidator {

    private final AuthServiceClient authClient;

    @Cacheable(value = "tokenCache", key = "#bearerToken")
    public String validateAndGetUserId(String bearerToken) {
        try {
            AuthResponse response = authClient.validateToken(bearerToken);
            return response.userId();
        } catch (FeignException.Unauthorized e) {
            throw new SecurityException("Invalid token");
        } catch (FeignException e) {
            throw new SecurityException("Auth service error: " + e.status() + " " + e.getMessage());
        }
    }
}
