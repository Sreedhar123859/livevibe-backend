package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.dto.AuthResponse;
import com.socialmedia.livevibe.dto.GoogleSignInRequest;
import com.socialmedia.livevibe.dto.GoogleTokenInfo;
import com.socialmedia.livevibe.entity.User;
import com.socialmedia.livevibe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleAuthServiceTest {

    @Mock
    private GoogleTokenVerifierService googleTokenVerifierService;

    @Mock
    private UserRepository userRepository;

    private GoogleAuthService googleAuthService;

    @BeforeEach
    void setUp() {
        JwtService jwtService = new JwtService(
                "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXowMTIzNDU2Nzg5QUJDREVGR0hJSg==",
                86400000L
        );
        googleAuthService = new GoogleAuthService(googleTokenVerifierService, userRepository, jwtService);
    }

    @Test
    void signInCreatesNewUserWhenEmailIsNotRegistered() {
        GoogleTokenInfo tokenInfo = new GoogleTokenInfo(
                "https://accounts.google.com",
                "test-google-client-id",
                "google-subject-123",
                "NewUser@example.com",
                true,
                "New User",
                "https://example.com/photo.jpg",
                String.valueOf(Long.MAX_VALUE)
        );
        when(googleTokenVerifierService.verify("google-id-token")).thenReturn(tokenInfo);
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("user-1");
            return user;
        });

        AuthResponse response = googleAuthService.signIn(new GoogleSignInRequest("google-id-token"));

        assertNotNull(response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getUser());
        assertEquals("user-1", response.getUser().getId());
        assertEquals("New User", response.getUser().getName());
        assertEquals("USER", response.getUser().getRole());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("newuser@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    void signInReturnsExistingUserWhenEmailAlreadyExists() {
        GoogleTokenInfo tokenInfo = new GoogleTokenInfo(
                "https://accounts.google.com",
                "test-google-client-id",
                "existing-google-subject",
                "existing@example.com",
                true,
                "Existing User",
                "https://example.com/photo.jpg",
                String.valueOf(Long.MAX_VALUE)
        );
        User existingUser = new User(
                "existing-id",
                "Existing User",
                "existing@example.com",
                "https://example.com/current-photo.jpg",
                "ADMIN"
        );

        when(googleTokenVerifierService.verify("existing-token")).thenReturn(tokenInfo);
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        AuthResponse response = googleAuthService.signIn(new GoogleSignInRequest("existing-token"));

        assertNotNull(response.getToken());
        assertEquals("existing-id", response.getUser().getId());
        assertEquals("ADMIN", response.getUser().getRole());
        verify(userRepository, never()).save(any(User.class));
    }
}

