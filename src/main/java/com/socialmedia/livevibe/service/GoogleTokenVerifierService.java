package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.dto.GoogleTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Set;

@Service
public class GoogleTokenVerifierService {
    private static final Logger log = LoggerFactory.getLogger(GoogleTokenVerifierService.class);
    private static final Set<String> ALLOWED_ISSUERS = Set.of("accounts.google.com", "https://accounts.google.com");

    private final RestClient restClient;
    private final String googleClientId;

    public GoogleTokenVerifierService(@Value("${app.google.client-id:}") String googleClientId) {
        this.restClient = RestClient.builder()
                .baseUrl("https://oauth2.googleapis.com")
                .build();
        this.googleClientId = googleClientId == null ? "" : googleClientId.trim();
    }

    public GoogleTokenInfo verify(String idToken) {
        if (googleClientId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Google sign-in is not configured");
        }

        try {
            GoogleTokenInfo tokenInfo = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/tokeninfo")
                            .queryParam("id_token", idToken)
                            .build())
                    .retrieve()
                    .body(GoogleTokenInfo.class);

            validateTokenInfo(tokenInfo);
            return tokenInfo;
        } catch (RestClientException ex) {
            log.warn("Google token verification failed: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google token");
        }
    }

    private void validateTokenInfo(GoogleTokenInfo tokenInfo) {
        if (tokenInfo == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google token");
        }
        if (!googleClientId.equals(tokenInfo.getAud())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token audience mismatch");
        }
        if (!ALLOWED_ISSUERS.contains(tokenInfo.getIss())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token issuer is invalid");
        }
        if (!Boolean.TRUE.equals(tokenInfo.getEmailVerified())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google email is not verified");
        }
        if (isBlank(tokenInfo.getSub()) || isBlank(tokenInfo.getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token is missing required claims");
        }
        if (isExpired(tokenInfo.getExp())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token is expired");
        }
    }

    private boolean isExpired(String exp) {
        try {
            long expiresAtEpochSeconds = Long.parseLong(exp);
            return Instant.ofEpochSecond(expiresAtEpochSeconds).isBefore(Instant.now());
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token expiration is invalid");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

