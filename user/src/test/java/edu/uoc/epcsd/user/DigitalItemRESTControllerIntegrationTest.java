package edu.uoc.epcsd.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.application.rest.request.CreateDigitalItemRequest;
import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.application.rest.request.CreateUserRequest;
import edu.uoc.epcsd.user.domain.DigitalItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class DigitalItemRESTControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenAddDigitalSessionWithItem_thenCanRetrieveItemsBySessionViaHttp() {
        // Arrange
        CreateUserRequest userRequest = new CreateUserRequest(
                "Test User",
                "testuser@example.com",
                "password",
                "123456789"
        );
        ResponseEntity<Long> userResponse = restTemplate.postForEntity("/users", userRequest, Long.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long userId = userResponse.getBody();
        assertThat(userId).isNotNull();

        // Act
        CreateDigitalSessionRequest sessionRequest = new CreateDigitalSessionRequest(
                userId,
                "Test session",
                "Test location",
                "http://test-link"
        );
        ResponseEntity<Long> sessionResponse = restTemplate.postForEntity("/digital/createDigital", sessionRequest, Long.class);
        assertThat(sessionResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long sessionId = sessionResponse.getBody();
        assertThat(sessionId).isNotNull();

        // Act
        CreateDigitalItemRequest itemRequest = new CreateDigitalItemRequest(
                sessionId,
                "Test item",
                123L,
                456L,
                "http://item-link"
        );
        ResponseEntity<Long> itemResponse = restTemplate.postForEntity("/digitalItem/addItem", itemRequest, Long.class);
        assertThat(itemResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long itemId = itemResponse.getBody();
        assertThat(itemId).isNotNull();

        // Assert
        ResponseEntity<List<DigitalItem>> getResponse = restTemplate.exchange(
                "/digitalItem/digitalItemBySession?digitalSessionId=" + sessionId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DigitalItem>>() {}
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<DigitalItem> items = getResponse.getBody();
        assertThat(items).isNotNull();
        assertThat(items).anyMatch(i -> i.getDescription().equals("Test item") && i.getDigitalSessionId().equals(sessionId));
    }
} 