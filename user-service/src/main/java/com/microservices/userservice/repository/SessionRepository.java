package com.microservices.userservice.repository;

import com.microservices.userservice.model.Session;
import com.microservices.userservice.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);

    @Override
    List<Session> findAll();

    Optional<List<Session>> findByUserIdAndSessionStatus(Long id, SessionStatus status);

    Optional<Session> findByUserIdAndToken(Long userId, String token);
}
