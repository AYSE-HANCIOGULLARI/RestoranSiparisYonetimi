package com.restoran.restoransiparisyonetimi.repository;

import com.restoran.restoransiparisyonetimi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}