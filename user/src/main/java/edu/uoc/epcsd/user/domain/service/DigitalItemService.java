package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalItem;

import java.util.List;
import java.util.Optional;

public interface DigitalItemService {

    List<DigitalItem> findAllDigitalItem();

    List<DigitalItem> findDigitalItemBySession(Long digitalSessionId);
    
	Optional<DigitalItem> getDigitalItemById(Long id);

	Long addDigitalItem(DigitalItem digitalItem);
    
    Long updateDigitalItem(Long digitalItemId, String description, String link, Long lat, Long lon);
    
    void dropDigitalItem(Long id);
    
    void setDigitalItemForReview(Long digitalItemId);
    
    void approvePendingDigitalItem(Long digitalItemId);
    
    void rejectPendingDigitalItem(Long digitalItemId);

}
