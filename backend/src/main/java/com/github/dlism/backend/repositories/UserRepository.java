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
    @Query(value = "insert into user_activation_code (code, user_id) values(:code, :user_id)", nativeQuery = true)
    void saveActivationCode(@Param("code") String code, @Param("user_id") Long user_id);

    @Query(value = "select * from usr where id=(select user_id from user_activation_code where code=:code)", nativeQuery = true)
    Optional<User> getUserByActivationCode(@Param("code") String code);
}
