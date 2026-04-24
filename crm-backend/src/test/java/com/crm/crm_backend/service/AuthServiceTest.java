package com.crm.crm_backend.service;

import com.crm.crm_backend.config.JwtUtil;
import com.crm.crm_backend.dto.AuthResponse;
import com.crm.crm_backend.dto.LoginRequest;
import com.crm.crm_backend.dto.RegisterRequest;
import com.crm.crm_backend.model.Role;
import com.crm.crm_backend.model.User;
import com.crm.crm_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@crm.com");
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(Role.REP);
    }

    @Test
    void register_shouldReturnToken_whenEmailNotExists() {
        // Arrange — pripremi podatke i mock ponašanje
        RegisterRequest request = new RegisterRequest();
        request.setEmail("novi@crm.com");
        request.setPassword("password123");
        request.setFirstName("Novi");
        request.setLastName("Korisnik");

        when(userRepository.existsByEmail("novi@crm.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        // Act — pozovi metod koji testiraš
        AuthResponse response = authService.register(request);

        // Assert — proveri rezultat
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@crm.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");

        when(userRepository.existsByEmail("test@crm.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Email već postoji", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@crm.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@crm.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword"))
                .thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("test@crm.com", response.getEmail());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("nepostoji@crm.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("nepostoji@crm.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Korisnik nije pronađen", exception.getMessage());
    }

    @Test
    void login_shouldThrowException_whenPasswordWrong() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@crm.com");
        request.setPassword("pogresnaLozinka");

        when(userRepository.findByEmail("test@crm.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("pogresnaLozinka", "hashedPassword"))
                .thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Pogrešna lozinka", exception.getMessage());
    }
}