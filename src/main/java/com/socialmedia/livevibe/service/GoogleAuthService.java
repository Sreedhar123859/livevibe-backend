package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.dto.AuthResponse;
import com.socialmedia.livevibe.dto.AuthUserDto;
import com.socialmedia.livevibe.dto.GoogleSignInRequest;
import com.socialmedia.livevibe.dto.GoogleTokenInfo;
import com.socialmedia.livevibe.entity.User;
import com.socialmedia.livevibe.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService {
    private static final Logger log = LoggerFactory.getLogger(GoogleAuthService.class);
    private static final String GOOGLE_PHONE_PREFIX = "google:";

    private final GoogleTokenVerifierService googleTokenVerifierService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public GoogleAuthService(GoogleTokenVerifierService googleTokenVerifierService,
                             UserRepository userRepository,
                             JwtService jwtService) {
        this.googleTokenVerifierService = googleTokenVerifierService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse signIn(GoogleSignInRequest request) {
        GoogleTokenInfo tokenInfo = googleTokenVerifierService.verify(request.getIdToken());
        String email = normalizeEmail(tokenInfo.getEmail());
        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser, tokenInfo))
                .orElseGet(() -> createUser(tokenInfo, email));

        String token = jwtService.generateToken(user);
        AuthUserDto userDto = toAuthUserDto(user);
        return new AuthResponse(token, "Bearer", userDto);
    }

    private User updateExistingUser(User user, GoogleTokenInfo tokenInfo) {
        boolean updated = false;

        if (isBlank(user.getName()) && !isBlank(tokenInfo.getName())) {
            user.setName(tokenInfo.getName().trim());
            updated = true;
        }
        if (isBlank(user.getProfileImage()) && !isBlank(tokenInfo.getPicture())) {
            user.setProfileImage(tokenInfo.getPicture().trim());
            updated = true;
        }

        if (updated) {
            log.info("Updated existing user {} during Google sign-in", user.getId());
            return userRepository.save(user);
        }

        return user;
    }

    private User createUser(GoogleTokenInfo tokenInfo, String email) {
        User user = new User(
                null,
                resolveName(tokenInfo),
                email,
                normalizeOptional(tokenInfo.getPicture()),
                "USER"
        );

        User savedUser = userRepository.save(user);
        log.info("Created new user {} via Google sign-in", savedUser.getId());
        return savedUser;
    }

    private String resolveName(GoogleTokenInfo tokenInfo) {
        if (!isBlank(tokenInfo.getName())) {
            return tokenInfo.getName().trim();
        }

        String email = normalizeEmail(tokenInfo.getEmail());
        int atIndex = email.indexOf('@');
        return atIndex > 0 ? email.substring(0, atIndex) : email;
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String generateSyntheticPhone(String subject) {
        return GOOGLE_PHONE_PREFIX + subject;
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public AuthUserDto toAuthUserDto(User user) {
        return new AuthUserDto(
                user.getId(),
                user.getName(),
                user.getProfileImage(),
                user.getRole()
        );
    }
}

