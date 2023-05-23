package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);

    boolean existsByName(String username);

    @Query("select u.organization from User u where u.id = ?1")
    Optional<Organization> findOrganizationByUserId(Long userId);

    long count();
}
