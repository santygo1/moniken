package ru.moniken.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collection")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class RouteCollection {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "collection_id")
    String id;

    @Column(unique = true)
    String name;

    @JsonIgnore
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    List<Route> routes = new ArrayList<>();

    String description;
}
