package com.github.dlism.backend.repositories;

import com.github.dlism.backend.models.UserActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationCodeRepository extends JpaRepository<UserActivationCode, Long> {
    Optional<UserActivationCode> findByCode(String code);

    void deleteByCode(String coed);
}
