package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DigitalSessionRepositoryImpl implements DigitalSessionRepository {

    private final SpringDataDigitalSessionRepository jpaDigitalSessionRepository;

    private final SpringDataUserRepository jpaUserRepository;

    @Override
    public List<DigitalSession> findAllDigitalSession() {
        return jpaDigitalSessionRepository.findAll().stream().map(DigitalSessionEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DigitalSession> getDigitalSessionById(Long id) {
        return jpaDigitalSessionRepository.getDigitalSessionById(id).map(DigitalSessionEntity::toDomain);
    }
    
    @Override
    public List<DigitalSession> findDigitalSessionByUser(Long userId) {
        return jpaDigitalSessionRepository.findDigitalSessionByUser(userId).stream().map(DigitalSessionEntity::toDomain).collect(Collectors.toList());
    }   

    @Override
    public Long createDigitalSession(DigitalSession digitalSession) {
        UserEntity userEntity = jpaUserRepository.findById(digitalSession.getUserId()).orElseThrow(IllegalArgumentException::new);
        DigitalSessionEntity digitalsessionEntity = DigitalSessionEntity.fromDomain(digitalSession);
        digitalsessionEntity.setUser(userEntity);
        
        return jpaDigitalSessionRepository.save(digitalsessionEntity).getId(); 
    }

	@Override
	public Long updateDigitalSession(DigitalSession digitalSession) {
        DigitalSessionEntity digitalSessionEntity1 = jpaDigitalSessionRepository.findById(digitalSession.getId()).orElseThrow(IllegalArgumentException::new);
        
        UserEntity userEntity = jpaUserRepository.findById(digitalSession.getUserId()).orElseThrow(IllegalArgumentException::new);
        digitalSessionEntity1.setUser(userEntity);
        digitalSessionEntity1.setDescription(digitalSession.getDescription());
        digitalSessionEntity1.setLocation(digitalSession.getLocation());
        digitalSessionEntity1.setLink(digitalSession.getLink());
        
        return jpaDigitalSessionRepository.save(digitalSessionEntity1).getId(); 
	}

	@Override
	public void removeDigitalSession(DigitalSession digitalSession) {   
		jpaDigitalSessionRepository.delete(DigitalSessionEntity.fromDomain(digitalSession));		
	}
	
    
}
