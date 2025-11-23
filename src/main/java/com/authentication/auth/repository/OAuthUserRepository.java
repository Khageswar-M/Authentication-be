package com.authentication.auth.repository;

import com.authentication.auth.model.OAutheUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAutheUser, Long> {
    Optional<OAutheUser> findByEmail(String email);

    @Modifying
    @Transactional
    void deleteByEmail(String email);
}
