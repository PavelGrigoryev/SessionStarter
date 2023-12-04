package ru.clevertec.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.session.dto.BlackListResponse;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.exception.SessionServiceException;
import ru.clevertec.session.mapper.SessionMapper;
import ru.clevertec.session.model.BlackList;
import ru.clevertec.session.repository.BlackListRepository;
import ru.clevertec.session.repository.SessionRepository;
import ru.clevertec.session.service.SessionService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final BlackListRepository blackListRepository;
    private final SessionMapper sessionMapper;

    @Override
    @Transactional
    public SessionResponse findByLoginOrSaveAndReturn(SessionRequest request) {
        return sessionRepository.findByLogin(request.login())
                .map(sessionMapper::toResponse)
                .orElseGet(() -> save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public BlackListResponse findAllBlackLists() {
        return new BlackListResponse(blackListRepository.findAll()
                .stream()
                .map(BlackList::getLogin)
                .collect(Collectors.toSet()));
    }

    public SessionResponse save(SessionRequest request) {
        return Optional.of(request)
                .map(sessionMapper::fromRequest)
                .map(sessionRepository::save)
                .map(sessionMapper::toResponse)
                .orElseThrow(() -> new SessionServiceException("Can't save session"));
    }

    @Override
    @Transactional
    public String deleteAll() {
        LocalDateTime now = LocalDateTime.now();
        Integer deletedCount = sessionRepository.deleteAllByOpeningTimeBefore(now);
        return deletedCount > 0
                ? "Cleared %s sessions until %s".formatted(deletedCount, now)
                : "No sessions to delete";
    }

}
