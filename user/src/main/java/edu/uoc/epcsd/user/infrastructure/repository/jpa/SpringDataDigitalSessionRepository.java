package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataDigitalSessionRepository extends JpaRepository<DigitalSessionEntity, Long> {

	@Query("select a from DigitalSession a where a.user.id = ?1 ")
	public List<DigitalSessionEntity> findDigitalSessionByUser(Long userId);
	
	//@Query("select a from DigitalSession a where a.id = ?1 ")
	public Optional<DigitalSessionEntity> getDigitalSessionById(Long id);
    
}
