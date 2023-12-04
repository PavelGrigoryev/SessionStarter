package ru.clevertec.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.session.model.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
