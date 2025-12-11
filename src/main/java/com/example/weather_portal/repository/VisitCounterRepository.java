package com.example.weather_portal.repository;

import com.example.weather_portal.model.VisitCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitCounterRepository extends JpaRepository<VisitCounter, Long> {

    Optional<VisitCounter> findByUserId(Long userId);
}

