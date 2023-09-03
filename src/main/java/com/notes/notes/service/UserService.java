package com.notes.notes.service;

import com.notes.notes.entity.Note;
import com.notes.notes.entity.User;
import org.hibernate.loader.ast.spi.MultiKeyLoadSizingStrategy;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User findById(Long id);
    User findByName(String name);
    User findByEmail(String email);
    User save(User user);
    User delete(Long id);
    User update(User user);
    List<Note> getAllNotes(Long id);
}
