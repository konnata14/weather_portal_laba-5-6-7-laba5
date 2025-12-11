package com.example.weather_portal.service;

import com.example.weather_portal.model.User;
import com.example.weather_portal.model.VisitCounter;
import com.example.weather_portal.repository.VisitCounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

    private final VisitCounterRepository repo;

    public VisitService(VisitCounterRepository repo) {
        this.repo = repo;
    }

    public VisitCounter getCounterFor(User user) {
        return repo.findByUserId(user.getId())
                .orElseGet(() -> {
                    VisitCounter v = new VisitCounter();
                    v.setUser(user);
                    v.setCount(0L);
                    return repo.save(v);
                });
    }

    @Transactional
    public Long increment(User user) {
        VisitCounter v = getCounterFor(user);
        v.setCount(v.getCount() + 1);
        return repo.save(v).getCount();
    }
}
