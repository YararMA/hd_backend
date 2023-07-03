package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.OrganizationPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByAuth(User user);

    boolean existsOrganizationsByAuth(User user);

    long count();

    @Query("select new com.github.dlism.backend.pojo.OrganizationPojo(o.id, o.name, o.description, o.active) from Organization o")
    List<OrganizationPojo> getAll();

    @Query(value = "select o.id, o.name, o.active, o.description, o.participants_max_count, count(suo.user_id) as subscribers from organization o\n" +
            "left join subscribing_user_organization suo on o.id = suo.organization_id where o.active=true\n" +
            "group by o.name, o.id", nativeQuery = true)
    List<Map<String, Object>> getAllActiveWithSubscribers();
}