package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataDigitalItemRepository extends JpaRepository<DigitalItemEntity, Long> {

	@Query("select a from DigitalItem a where a.digitalSession.id = ?1 ")
	public List<DigitalItemEntity> findDigitalItemByDigitalSession(Long digitalSessionId);
	
	//@Query("select a from DigitalItem a where a.id = ?1 ")
	public Optional<DigitalItemEntity> getDigitalItemById(Long id);
    
}
