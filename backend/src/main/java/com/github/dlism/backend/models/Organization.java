package com.github.dlism.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(columnDefinition = "boolean default false")
    private boolean active;
}
