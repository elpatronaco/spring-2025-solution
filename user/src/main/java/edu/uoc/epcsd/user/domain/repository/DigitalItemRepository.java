package edu.uoc.epcsd.user.domain.repository;

import edu.uoc.epcsd.user.domain.DigitalItem;

import java.util.List;
import java.util.Optional;

public interface DigitalItemRepository {

    List<DigitalItem> findDigitalItemBySession(Long digitalSessionId);

    Long createDigitalItem(DigitalItem digitalItem);
    
    Long updateDigitalItem(DigitalItem digitalItem);
	
    void removeDigitalItem(DigitalItem digitalItem);

	List<DigitalItem> findAllDigitalItem();

	Optional<DigitalItem> getDigitalItemById(Long id);
	
}
