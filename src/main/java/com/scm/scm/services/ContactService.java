package com.scm.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    //get contacts
    List<Contact> getAll();
    //get contact by id
    Contact getById(String id);

    void delete(String id);

    //search contact
    Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, String user);
    Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String order, String user);
    Page<Contact> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String order, String user);

    //get contacts by userId
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user,int page, int size, String sortBy, String direction);
}
