package com.calendiary.calendiary_backend.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.base-url}"
)
public interface AuthServiceClient {

    @PostMapping("/auth/validate")
    AuthResponse validateToken(@RequestHeader("Authorization") String token);
}
