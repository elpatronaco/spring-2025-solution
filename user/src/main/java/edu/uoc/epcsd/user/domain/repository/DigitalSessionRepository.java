package edu.uoc.epcsd.user.domain.repository;

import edu.uoc.epcsd.user.domain.DigitalSession;

import java.util.List;
import java.util.Optional;

public interface DigitalSessionRepository {

    List<DigitalSession> findDigitalSessionByUser(Long userId);

    Long createDigitalSession(DigitalSession digitalSession);
    
    Long updateDigitalSession(DigitalSession digitalSession);
	
    void removeDigitalSession(DigitalSession digitalSession);

	List<DigitalSession> findAllDigitalSession();

	Optional<DigitalSession> getDigitalSessionById(Long id);
	
}
