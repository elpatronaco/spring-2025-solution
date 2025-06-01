package edu.uoc.epcsd.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.List;

import edu.uoc.epcsd.user.application.rest.DigitalItemRESTController;
import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalItemRESTControllerUnitTest {

    DigitalItemRESTController digitalItemRESTController;
    DigitalItemService digitalItemService;

    @BeforeEach
    void setUp() {
        digitalItemService = mock(DigitalItemService.class);

        digitalItemRESTController = new DigitalItemRESTController(digitalItemService);
    }

    @Test
    @DisplayName("Should return items by session id")
    void shouldReturnItemsBySessionId() {
        // Arrange
        Long sessionId = 1L;
        DigitalItem item1 = DigitalItem.builder()
                .id(10L)
                .digitalSessionId(sessionId)
                .description("Item 1")
                .lat(123L)
                .lon(456L)
                .link("http://item1.com")
                .build();
        DigitalItem item2 = DigitalItem.builder()
                .id(11L)
                .digitalSessionId(sessionId)
                .description("Item 2")
                .lat(789L)
                .lon(101L)
                .link("http://item2.com")
                .build();
        List<DigitalItem> expectedItems = List.of(item1, item2);
        when(digitalItemService.findDigitalItemBySession(sessionId)).thenReturn(expectedItems);

        // Act
        List<DigitalItem> result = digitalItemRESTController.findDigitalItemBySession(sessionId);

        // Assert
        assertEquals(expectedItems, result);
        verify(digitalItemService).findDigitalItemBySession(sessionId);
    }
}
