package com.scm.scm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,String>{

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

}
