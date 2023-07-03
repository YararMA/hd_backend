package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.UserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    long count();

    @Query("select new com.github.dlism.backend.pojo.UserPojo(u.id, u.username) from User u")
    List<UserPojo> all();

    @Modifying
    @Query(value = "insert into subscribing_user_organization (user_id, organization_id) values(:user_id, :organization_id)", nativeQuery = true)
    void joinToOrganization(@Param("user_id") Long userId, @Param("organization_id") Long organizationId);

    @Modifying
    @Query("update User set username = :#{#user.username}, firstname = :#{#user.firstname}, lastname = :#{#user.lastname}, name = :#{#user.name}, phone = :#{#user.phone}, gender = :#{#user.gender}, age = :#{#user.age} where id = :#{#user.id}")
    void update(@Param("user") User user);

    @Modifying
    @Query("update User set password = :password where id = :user_id")
    void  changePassword(@Param("user_id") Long user_id, @Param("password") String password);
}
