package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalItemRequest;
import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for DigitalItemRESTController using real HTTP requests against PostgreSQL database.
 * 
 * Prerequisites:
 * 1. Start the database: docker-compose up -d userdb
 * 2. Run this test with the "integration" profile
 * 
 * This test validates that adding DigitalItems to a DigitalSession via HTTP requests
 * allows proper retrieval using the REST endpoint for findDigitalItemBySession.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Transactional
public class DigitalItemRESTControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void whenAddDigitalItemToSession_thenCanRetrieveBySessionViaHTTP_UsingRealDatabase() {
        // Arrange
        CreateDigitalSessionRequest sessionRequest = new CreateDigitalSessionRequest(
                1L,
                "HTTP Test session",
                "HTTP Test location",
                "http://http-test-link"
        );

        ResponseEntity<Long> sessionResponse = restTemplate.postForEntity(
                baseUrl + "/digital/createDigital",
                sessionRequest,
                Long.class
        );

        assertThat(sessionResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sessionResponse.getBody()).isNotNull();
        Long sessionId = sessionResponse.getBody();

        // Act
        CreateDigitalItemRequest itemRequest1 = new CreateDigitalItemRequest(
                sessionId,
                "HTTP Test item 1",
                123L,
                456L,
                "http://http-item1-link"
        );

        CreateDigitalItemRequest itemRequest2 = new CreateDigitalItemRequest(
                sessionId,
                "HTTP Test item 2",
                789L,
                101112L,
                "http://http-item2-link"
        );

        ResponseEntity<Long> item1Response = restTemplate.postForEntity(
                baseUrl + "/digitalItem/addItem",
                itemRequest1,
                Long.class
        );

        ResponseEntity<Long> item2Response = restTemplate.postForEntity(
                baseUrl + "/digitalItem/addItem",
                itemRequest2,
                Long.class
        );

        assertThat(item1Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(item2Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(item1Response.getBody()).isNotNull();
        assertThat(item2Response.getBody()).isNotNull();

        Long item1Id = item1Response.getBody();
        Long item2Id = item2Response.getBody();

        // Assert
        String getUrl = baseUrl + "/digitalItem/digitalItemBySession?digitalSessionId=" + sessionId;
        
        ResponseEntity<List<DigitalItem>> getResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DigitalItem>>() {}
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        
        List<DigitalItem> items = getResponse.getBody();
        
        // Verify the results
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(2);
        
        assertThat(items).anyMatch(item -> 
            item.getId().equals(item1Id) && 
            item.getDescription().equals("HTTP Test item 1") &&
            item.getStatus() == DigitalItemStatus.AVAILABLE &&
            item.getDigitalSessionId().equals(sessionId)
        );
        
        assertThat(items).anyMatch(item -> 
            item.getId().equals(item2Id) && 
            item.getDescription().equals("HTTP Test item 2") &&
            item.getStatus() == DigitalItemStatus.AVAILABLE &&
            item.getDigitalSessionId().equals(sessionId)
        );

        // verifiquem que tots els items pertanyen a la sessió
        items.forEach(item -> {
            assertThat(item.getDigitalSessionId()).isEqualTo(sessionId);
            assertThat(item.getLat()).isNotNull();
            assertThat(item.getLon()).isNotNull();
            assertThat(item.getLink()).isNotNull();
        });
    }

    @Test
    void whenQueryNonExistentSession_thenReturnEmptyListViaHTTP_UsingRealDatabase() {
        // Act
        String getUrl = baseUrl + "/digitalItem/digitalItemBySession?digitalSessionId=999999";
        
        ResponseEntity<List<DigitalItem>> getResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DigitalItem>>() {}
        );

        // Assert
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody()).isEmpty();
    }

    @Test
    void whenAddItemToInvalidSession_thenReturnBadRequestViaHTTP_UsingRealDatabase() {
        // Act
        CreateDigitalItemRequest invalidItemRequest = new CreateDigitalItemRequest(
                999999L, // Non-existent session ID
                "Invalid item",
                123L,
                456L,
                "http://invalid-link"
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/digitalItem/addItem",
                invalidItemRequest,
                String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
} 