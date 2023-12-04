package ru.clevertec.session.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.session.dto.BlackListResponse;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> findByLoginOrSaveAndReturn(@RequestBody SessionRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.findByLoginOrSaveAndReturn(request));
    }

    @PostMapping("/blackList")
    public ResponseEntity<SessionResponse> addLoginToBlackList(@RequestBody SessionRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.addLoginToBlackList(request));
    }

    @GetMapping
    public ResponseEntity<BlackListResponse> findAllBlackLists() {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.findAllBlackLists());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        return ResponseEntity.ok().body(sessionService.deleteAll());
    }

}
