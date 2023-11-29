package ru.clevertec.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.session.model.Session;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByLogin(String login);

    @Modifying
    @Query("""
            DELETE FROM Session s
            WHERE s.openingTime < :dateTime
            """)
    Integer deleteAllByOpeningTimeBefore(LocalDateTime dateTime);

}
