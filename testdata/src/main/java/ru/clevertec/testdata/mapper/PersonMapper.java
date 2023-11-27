package ru.clevertec.testdata.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.testdata.dto.PersonRequest;
import ru.clevertec.testdata.dto.PersonResponse;
import ru.clevertec.testdata.model.Person;

@Mapper
public interface PersonMapper {

    Person fromRequest(PersonRequest request);

    PersonResponse toResponse(Person person);

}
