package ru.moniken.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.springframework.http.HttpStatus;
import ru.moniken.model.records.HTTPMethod;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"method", "endpoint"})})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String endpoint;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    Map<String, Object> body = new HashMap<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    HTTPMethod method = HTTPMethod.GET;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    HttpStatus status = HttpStatus.OK;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    Map<String, String> headers = new HashMap<>();

    @Column(nullable = false)
    int timeout = 0;
}
