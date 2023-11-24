package ru.moniken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moniken.model.entity.RouteCollection;

import java.util.Optional;

@Repository
public interface RouteCollectionRepository extends JpaRepository<RouteCollection, String> {
    Optional<RouteCollection> findByName(String Name);

    void deleteByName(String name);
}
