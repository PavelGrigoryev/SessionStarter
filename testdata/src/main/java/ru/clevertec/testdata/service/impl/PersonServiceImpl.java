package ru.clevertec.testdata.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.testdata.dto.PersonRequest;
import ru.clevertec.testdata.dto.PersonResponse;
import ru.clevertec.testdata.exception.NotFoundException;
import ru.clevertec.testdata.exception.ServiceException;
import ru.clevertec.testdata.exception.UniqueException;
import ru.clevertec.testdata.mapper.PersonMapper;
import ru.clevertec.testdata.repository.PersonRepository;
import ru.clevertec.testdata.service.PersonService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonResponse findById(Long id) {
        return personRepository.findById(id)
                .map(personMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Person with id %s is not found".formatted(id)));
    }

    @Override
    @Transactional
    public PersonResponse save(PersonRequest request) {
        return Optional.of(request)
                .map(personMapper::fromRequest)
                .map(person -> {
                    try {
                        return personRepository.save(person);
                    } catch (DataIntegrityViolationException e) {
                        throw new UniqueException("Person with login %s is already exist".formatted(request.login()));
                    }
                })
                .map(personMapper::toResponse)
                .orElseThrow(() -> new ServiceException("Can't save session"));
    }

}
