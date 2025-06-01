package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;

import java.util.List;
import java.util.Optional;

public interface DigitalSessionService {

    List<DigitalSession> findAllDigitalSession();

    List<DigitalSession> findDigitalSessionByUser(Long userId);
    
	Optional<DigitalSession> getDigitalSessionById(Long id);

	Long createDigitalSession(DigitalSession digitalsession);
    
    Long updateDigitalSession(Long digitalSessionId, String description, String link, String location, Long userId);
    
    void removeDigitalSession(Long id);

}
