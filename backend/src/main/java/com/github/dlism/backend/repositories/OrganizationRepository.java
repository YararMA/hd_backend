package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.pojo.OrganizationPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByName(String name);

    boolean existsByName(String username);

    @Query("select u.organization from User u where u.id = ?1")
    Optional<Organization> findByUserId(Long userId);

    long count();

    @Query("select new com.github.dlism.backend.pojo.OrganizationPojo(o.id, o.name, o.description, o.active) from Organization o")
    List<OrganizationPojo> all();
}