package com.example.simplespringboot.repository;

import com.example.simplespringboot.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

// for action entity
public interface UserRepository extends CrudRepository<User,String> {

    Optional<User> findByEmail(String s);
    Optional<User> findByToken(String token);
    boolean existsByEmail(String email);
}
