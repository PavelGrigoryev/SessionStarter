package ru.clevertec.session.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> save(@RequestBody @Valid SessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.save(request));
    }

    @GetMapping
    public ResponseEntity<SessionResponse> findByLogin(@RequestBody @Valid SessionRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.findByLogin(request));
    }

}
