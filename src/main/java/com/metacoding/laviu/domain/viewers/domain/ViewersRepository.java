package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ViewersRepository {
    private final EntityManager em;

    public void deleteById(Viewers viewer) {
        em.remove(viewer);
    }

    public Optional<Viewers> findById(String viewerId) {
        return Optional.ofNullable(em.find(Viewers.class, viewerId));
    }
}
