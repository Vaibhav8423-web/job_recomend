package com.scm.scm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact,String>{

    //find the contact by user
    Page<Contact> findByUser(User user, Pageable pageable);
    
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByNameContainingAndUser(String name, User user, Pageable pageable);
    Page<Contact> findByEmailContainingAndUser(String email, User user, Pageable pageable);
    Page<Contact> findByPhoneNumberContainingAndUser(String phoneNumber, User user, Pageable pageable);

}
