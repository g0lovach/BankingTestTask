package com.example.bankingtesttask.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Table(name = "Divisions")
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(name = "name", unique = true)
    String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "code", unique = true)
    String code;

    @Column(name = "bic", unique = true)
    String bic;

    @Column(name = "inn", unique = true)
    String inn;

    public Division() {

    }
}
