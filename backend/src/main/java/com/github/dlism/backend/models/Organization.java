package com.github.dlism.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "organization")
@Setter
@Getter
@ToString
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User auth;

    @OneToMany(mappedBy = "organization")
    private Set<User> users;

    private int participantsMaxCount;

    private String country;

    private String region;

    private String city;

    private String address;

    private String type;
}
