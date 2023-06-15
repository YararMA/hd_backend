package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.pojo.OrganizationPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("select u.organization from User u where u.id = ?1")
    Optional<Organization> findByUserId(Long userId);

    long count();

    @Query("select new com.github.dlism.backend.pojo.OrganizationPojo(o.id, o.name, o.description, o.active) from Organization o")
    List<OrganizationPojo> getAll();

    @Query("select new com.github.dlism.backend.pojo.OrganizationPojo(o.id, o.name, o.description, o.active) from Organization o where o.active=true ")
    List<OrganizationPojo> getAllActive();

    @Query(value = "select count(*) from subscribing_user_organization where organization_id=:id", nativeQuery = true)
    String subscribersCount(@Param("id") Long id);

    @Query(value = "select o.id, o.name, o.active, o.description, count(suo.user_id) as subscribers from organization o\n" +
            "    left join subscribing_user_organization suo on o.id = suo.organization_id\n" +
            "group by o.name, o.id", nativeQuery = true)
    List<Map<String, Object>> getAllActiveWithSubscribers();
}