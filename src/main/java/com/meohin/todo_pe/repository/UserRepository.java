package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUserId(String userId);
}
