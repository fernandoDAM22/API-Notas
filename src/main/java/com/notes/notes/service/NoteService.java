package com.notes.notes.service;

import com.notes.notes.entity.Category;
import com.notes.notes.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
     List<Note> getAll();
     List<Note> getAllByCategories(List<Long> categories);
     List<Note> getAllByCategory(Long id);
     List<Note> getAllByUser(Long id);
     Note findById(Long id);
     Note findByTitle(String title);
     Note save(Note note);
     Note delete(Long id);
     Note update(Note note);
     Category getCategory(Long id);
     boolean verify(String title,Long id);
}
