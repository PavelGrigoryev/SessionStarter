package ru.clevertec.testdata.service;

import ru.clevertec.testdata.dto.PersonRequest;
import ru.clevertec.testdata.dto.PersonResponse;

public interface PersonService {

    PersonResponse findById(Long id);

    PersonResponse save(PersonRequest request);

}
