package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.domain.artist.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    Optional<User> findBySeq(Long seq);
}