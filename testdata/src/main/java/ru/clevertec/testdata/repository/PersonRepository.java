package ru.clevertec.testdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.testdata.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
