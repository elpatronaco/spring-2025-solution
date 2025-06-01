package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DigitalItemServiceImpl implements DigitalItemService {   
    
    private final DigitalItemRepository digitalItemRepository;
    
    @Value("${userService.getUserById.url}")
    private String getUserById;   
    
    public List<DigitalItem> findAllDigitalItem() {
        return digitalItemRepository.findAllDigitalItem();
    }

    @Override
	public List<DigitalItem> findDigitalItemBySession(Long digitalSessionId) {    		
    	return digitalItemRepository.findDigitalItemBySession(digitalSessionId);
	}
    
    @Override
	public Optional<DigitalItem> getDigitalItemById(Long id) {
        return digitalItemRepository.getDigitalItemById(id);
	}

	@Override
	public Long addDigitalItem(DigitalItem digitalItem) {
        return digitalItemRepository.createDigitalItem(digitalItem);
		
	}

	@Override
	public Long updateDigitalItem(Long digitalItemId, String description, String link, Long lat, Long lon) {
       
        DigitalItem digitalItem = digitalItemRepository.getDigitalItemById(digitalItemId).orElseThrow(IllegalArgumentException::new);
 
        digitalItem.setDescription(description);
        digitalItem.setLink(link);
        digitalItem.setLon(lon);
        digitalItem.setLat(lat);
		
		return digitalItemRepository.updateDigitalItem(digitalItem);
	}

	@Override
	public void dropDigitalItem(Long id) {
		DigitalItem digitalItem = digitalItemRepository.getDigitalItemById(id).orElseThrow(IllegalArgumentException::new);
				
        digitalItemRepository.removeDigitalItem(digitalItem);		
	}

	@Override
	public void setDigitalItemForReview(Long digitalItemId) {
        DigitalItem digitalItem = digitalItemRepository.getDigitalItemById(digitalItemId).orElseThrow(IllegalArgumentException::new);
        
        digitalItem.setStatus(DigitalItemStatus.REVIEW_PENDING);
		
		digitalItemRepository.updateDigitalItem(digitalItem);
	}

	@Override
	public void approvePendingDigitalItem(Long digitalItemId) {
		DigitalItem digitalItem = digitalItemRepository.getDigitalItemById(digitalItemId).orElseThrow(IllegalArgumentException::new);
        if(!digitalItem.getStatus().equals(DigitalItemStatus.REVIEW_PENDING)) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digital item with ID " + digitalItemId + " is not under review status.");
        }
        digitalItem.setStatus(DigitalItemStatus.AVAILABLE);
		
		digitalItemRepository.updateDigitalItem(digitalItem);
	}

	@Override
	public void rejectPendingDigitalItem(Long digitalItemId) {
		DigitalItem digitalItem = digitalItemRepository.getDigitalItemById(digitalItemId).orElseThrow(IllegalArgumentException::new);
        if(!digitalItem.getStatus().equals(DigitalItemStatus.REVIEW_PENDING)) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digital item with ID " + digitalItemId + " is not under review status.");
        }
        digitalItem.setStatus(DigitalItemStatus.NOT_AVAILABLE);
		
		digitalItemRepository.updateDigitalItem(digitalItem);
		
	}
	
}
