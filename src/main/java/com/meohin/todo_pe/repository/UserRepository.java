package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
