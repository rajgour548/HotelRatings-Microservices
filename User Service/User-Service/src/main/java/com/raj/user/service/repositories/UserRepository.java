package com.raj.user.service.repositories;

import com.raj.user.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByAuth0Id(String auth0Id);
    boolean existsByUserEmail(String email);
    Optional<User> findByUserEmail(String email);
}
