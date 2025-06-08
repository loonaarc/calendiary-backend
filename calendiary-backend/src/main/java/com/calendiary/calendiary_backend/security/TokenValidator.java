package com.calendiary.calendiary_backend.security;

import com.calendiary.calendiary_backend.exceptions.InvalidQueryFormatException;
import com.calendiary.calendiary_backend.feign.AuthResponse;
import com.calendiary.calendiary_backend.feign.AuthServiceClient;
import com.calendiary.calendiary_backend.service.LabelService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Validates a JWT token by forwarding it to the external Auth service.
 * If the token is valid, extracts the user ID from the response.

 * Example expected response from the Auth service (/auth/validate):
 * {
 *   "userId": 123,
 *   "email": "john.doe@example.com",
 *   "name": "John Doe",
 *   "oauthUser": true,
 *   "faceId": 456
 * }
 */
@Service
@RequiredArgsConstructor
public class TokenValidator {

    private final AuthServiceClient authClient;
    private final LabelService labelService;

    /**
     * Validates the given Bearer token by sending it to the Auth service.
     * Uses caching to avoid redundant validation for the same token.
     *
     * @param bearerToken The full Bearer token string, e.g. "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."
     * @return userId (as String) if the token is valid
     * @throws SecurityException if the token is invalid or the Auth service fails
     */
    @Cacheable(value = "tokenCache", key = "#bearerToken")
    public String validateAndGetUserId(String bearerToken) {
        try {
            AuthResponse response = authClient.validateToken(bearerToken);

            String userId = String.valueOf(response.userId());

            try {
                labelService.createDefaultLabelsForUser(userId);
            } catch (InvalidQueryFormatException e) {
                System.err.println("Invalid userId format received from Auth Service: " + userId + " - " + e.getMessage());
                throw new SecurityException("Auth service returned invalid user ID format.");
            }
            // *****************************************************************

            return userId;

        } catch (FeignException.Unauthorized e) {
            throw new SecurityException("Invalid token");
        } catch (FeignException e) {
            throw new SecurityException("Auth service error: " + e.status() + " - " + e.getMessage());
        }
    }
}
