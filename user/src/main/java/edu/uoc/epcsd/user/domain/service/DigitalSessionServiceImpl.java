package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DigitalSessionServiceImpl implements DigitalSessionService {

    private final DigitalSessionRepository digitalSessionRepository;

    private final UserRepository userRepository;


    public List<DigitalSession> findAllDigitalSession() {
        return digitalSessionRepository.findAllDigitalSession();
    }

    @Override
    public List<DigitalSession> findDigitalSessionByUser(Long userId) {
        Optional<User> user = userRepository.findUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        return digitalSessionRepository.findDigitalSessionByUser(userId);
    }

    @Override
    public Optional<DigitalSession> getDigitalSessionById(Long id) {
        return digitalSessionRepository.getDigitalSessionById(id);
    }

    @Override
    public Long createDigitalSession(DigitalSession digitalSession) {
        return digitalSessionRepository.createDigitalSession(digitalSession);
    }

    @Override
    public Long updateDigitalSession(Long digitalSessionId, String description, String link, String location, Long userId) {
        DigitalSession digitalSession = digitalSessionRepository
                .getDigitalSessionById(digitalSessionId)
                .orElseThrow(IllegalArgumentException::new);

        digitalSession.setDescription(description);
        digitalSession.setLink(link);
        digitalSession.setLocation(location);
        digitalSession.setUserId(userId);

        return digitalSessionRepository.updateDigitalSession(digitalSession);
    }

    @Override
    public void removeDigitalSession(Long id) {
        DigitalSession digitalSession = digitalSessionRepository
                .getDigitalSessionById(id)
                .orElseThrow(IllegalArgumentException::new);
        digitalSessionRepository.removeDigitalSession(digitalSession);
    }

}
