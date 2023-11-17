package ru.moniken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moniken.model.entity.RouteEntity;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, String> {
    List<RouteEntity> findByEndpoint(String endpoint);
}
