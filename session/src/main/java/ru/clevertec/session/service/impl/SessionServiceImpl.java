package ru.clevertec.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.exception.NotFoundException;
import ru.clevertec.session.exception.ServiceException;
import ru.clevertec.session.exception.UniqueException;
import ru.clevertec.session.mapper.SessionMapper;
import ru.clevertec.session.repository.SessionRepository;
import ru.clevertec.session.service.SessionService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Override
    @Transactional
    public SessionResponse save(SessionRequest request) {
        return Optional.of(request)
                .map(sessionMapper::fromRequest)
                .map(session -> {
                    try {
                        return sessionRepository.save(session);
                    } catch (DataIntegrityViolationException e) {
                        throw new UniqueException("Session with login %s is already exist".formatted(request.login()));
                    }
                })
                .map(sessionMapper::toResponse)
                .orElseThrow(() -> new ServiceException("Can't save session"));
    }

    @Override
    public SessionResponse findByLogin(SessionRequest request) {
        return sessionRepository.findByLogin(request.login())
                .map(sessionMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Session with login %s is not found".formatted(request.login())));
    }

    @Override
    @Transactional
    public void deleteAllByOpeningTimeBefore(LocalDateTime dateTime) {
        sessionRepository.deleteAllByOpeningTimeBefore(dateTime);
    }

}
