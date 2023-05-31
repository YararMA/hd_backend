package com.github.dlism.backend.repositories;

import com.github.dlism.backend.dto.UserDto;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.UserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    long count();
    @Query("select new com.github.dlism.backend.pojo.UserPojo(u.id, u.username) from User u")
    List<UserPojo> all();
}
