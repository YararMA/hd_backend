package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);

    boolean existsByName(String username);
}
