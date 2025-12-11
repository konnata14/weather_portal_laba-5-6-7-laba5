package com.example.weather_portal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "visit_counter")
public class VisitCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count = 0L;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
