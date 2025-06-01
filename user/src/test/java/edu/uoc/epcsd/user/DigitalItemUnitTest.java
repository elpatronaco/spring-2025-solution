package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalItemUnitTest {
    @Test
    @DisplayName("Newly created DigitalItem should have status AVAILABLE")
    void digitalItemIsAvailableByDefault() {
        // Arrange
        DigitalItem item = DigitalItem.builder()
                .id(1L)
                .digitalSessionId(1L)
                .description("Test Item")
                .lat(123456789L)
                .lon(987654321L)
                .link("http://example.com")
                .build();

        // Act
        DigitalItemStatus status = item.getStatus();

        // Assert
        assertEquals(status, DigitalItemStatus.AVAILABLE);
    }


}
