package ru.moniken.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter @Setter @FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Route {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String endpoint;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    Map<String, Object> body = new HashMap<>();
}
