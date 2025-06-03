package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import edu.uoc.epcsd.user.domain.service.DigitalSessionService;
import edu.uoc.epcsd.user.domain.service.DigitalSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import edu.uoc.epcsd.user.application.rest.response.GetUserResponseTest;

public class DigitalSessionServiceUnitTest {
    DigitalSessionRepository digitalSessionRepository;
    UserRepository userRepository;
    DigitalSessionService digitalSessionService;

    @BeforeEach
    void setUp() {
        digitalSessionRepository = mock(DigitalSessionRepository.class);
        userRepository = mock(UserRepository.class);

        digitalSessionService = new DigitalSessionServiceImpl(digitalSessionRepository, userRepository);
    }

    @Test
    @DisplayName("Should obtain correct list of digital sessions by id")
    void shouldObtainCorrectListOfDigitalSessionsById() {
        // Arrange
        Long userId = 1L;
        List<DigitalSession> expectedSessions = List.of(
                DigitalSession.builder()
                        .id(1L)
                        .description("Test Session 1")
                        .link("http://test1.com")
                        .location("Test Location")
                        .userId(userId)
                        .build(),
                DigitalSession.builder()
                        .id(2L)
                        .description("Test Session 2")
                        .link("http://test2.com")
                        .location("Test Location")
                        .userId(userId)
                        .build());

        // Mock the user repository to return a user
        User user = User.builder()
                .id(userId)
                .fullName("Test User")
                .email("test@example.com")
                .password("password")
                .phoneNumber("123456789")
                .build();
                
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(digitalSessionRepository.findDigitalSessionByUser(userId)).thenReturn(expectedSessions);

        GetUserResponseTest userResponse = GetUserResponseTest.builder()
                .id(userId)
                .fullName("Test User")
                .email("test@example.com")
                .phoneNumber("123456789")
                .build();

        // Act
        List<DigitalSession> sessions = digitalSessionService.findDigitalSessionByUser(userId);

        // Assert
        assertEquals(expectedSessions.size(), sessions.size());
        for (DigitalSession session : sessions) {
            assertEquals(userId, session.getUserId());
        }
        verify(digitalSessionRepository, times(1)).findDigitalSessionByUser(userId);
    }

    @Test
    @DisplayName("Should throw an exception if user id is not found")
    void shouldThrowExceptionIfUserIdIsNotFound() {
        // Arrange
        Long userId = 1L;
        
        // Mock user repository to return empty (user not found)
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());
        
        // We don't need to mock digitalSessionRepository here since the exception will be thrown before it's called

        // Act & Assert - should throw UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> digitalSessionService.findDigitalSessionByUser(userId));
    }
}
