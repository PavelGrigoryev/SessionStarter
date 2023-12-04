package ru.clevertec.testdata.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.starter.annotation.SessionAware;
import ru.clevertec.starter.model.Authorization;
import ru.clevertec.starter.model.Session;
import ru.clevertec.testdata.dto.PersonRequest;
import ru.clevertec.testdata.dto.PersonResponse;
import ru.clevertec.testdata.service.PersonService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @SessionAware(blackList = {"Ann", "Sasha"})
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                   Authorization authorization,
                                                   @PathVariable
                                                   Long id,
                                                   Session session) {
        log.info(session.toString());
        return ResponseEntity.ok(personService.findById(id));
    }

    @SessionAware
    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody PersonRequest request, Session session) {
        log.info(session.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.save(request));
    }

}
