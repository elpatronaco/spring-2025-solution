package edu.uoc.epcsd.user;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DigitalItemRepositoryIntegrationTest {

    @Autowired
    private DigitalItemRepository digitalItemRepository;

    @Autowired
    private DigitalSessionRepository digitalSessionRepository;

    @Test
    void whenAddDigitalItemToSession_thenCanRetrieveBySession() {
        // Arrange
        DigitalSession session = DigitalSession.builder()
                .description("Test session")
                .location("Test location")
                .link("http://test-link")
                .userId(1L)
                .build();
        Long sessionId = digitalSessionRepository.createDigitalSession(session);

        // Act
        DigitalItem item = DigitalItem.builder()
                .description("Test item")
                .lat(123L)
                .lon(456L)
                .link("http://item-link")
                .status(DigitalItemStatus.AVAILABLE)
                .digitalSessionId(sessionId)
                .build();
        Long itemId = digitalItemRepository.createDigitalItem(item);

        // Assert
        List<DigitalItem> items = digitalItemRepository.findDigitalItemBySession(sessionId);
        assertThat(items).isNotEmpty();
        assertThat(items).anyMatch(i -> i.getId().equals(itemId) && i.getDescription().equals("Test item"));
    }
} 