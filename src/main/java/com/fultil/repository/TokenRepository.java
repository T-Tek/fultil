package com.fultil.repository;

import com.fultil.entity.AccountActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AccountActivationToken, Long> {
    Optional<AccountActivationToken> findByToken(String token);
}
