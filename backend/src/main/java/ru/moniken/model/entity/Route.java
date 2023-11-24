package ru.moniken.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.springframework.http.HttpStatus;
import ru.moniken.model.records.HttpMethod;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "route", uniqueConstraints = {@UniqueConstraint(columnNames = {"method", "endpoint"})})
@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Route {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    // Если имя не указано, то заменяется на endpoint
    // (см. RouteManagerService.commitRouteOrExcept)
    String name;

    @Column(nullable = false)
    String endpoint;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    Map<String, Object> body = new HashMap<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    HttpMethod method = HttpMethod.GET;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    HttpStatus status = HttpStatus.OK;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    Map<String, String> headers = new HashMap<>();

    @Column(nullable = false)
    int timeout = 0;

    String description;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    RouteCollection collection;
}
