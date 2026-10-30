package com.socialmedia.livevibe.controller;

import com.socialmedia.livevibe.dto.AuthResponse;
import com.socialmedia.livevibe.dto.AuthUserDto;
import com.socialmedia.livevibe.dto.GoogleSignInRequest;
import com.socialmedia.livevibe.entity.User;
import com.socialmedia.livevibe.service.GoogleAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class GoogleAuthController {
    private static final Logger log = LoggerFactory.getLogger(GoogleAuthController.class);

    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody GoogleSignInRequest request) {
        log.info("Received Google sign-in request");
        return ResponseEntity.ok(googleAuthService.signIn(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDto> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        log.debug("Fetched current user profile for userId {}", user.getId());
        return ResponseEntity.ok(googleAuthService.toAuthUserDto(user));
    }
}

