package com.notes.notes.repository;

import com.notes.notes.entity.Note;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    @Query(value = "Select * from notes where title like :title limit 1",nativeQuery = true)
    Optional<Note> findByTitle(@Param("title") String title);
    @Query(value = "Select * from notes where category_id = :id", nativeQuery = true)
    List<Note> getNotesByCategory(@Param("id") Long id);
    @Query(value = "SELECT * FROM notes where category_id in :categories", nativeQuery = true)
    List<Note> getAllByCategories(@Param("categories") List<Long> categories);

    @Query(value = "SELECT * from notes where user_id = :id", nativeQuery = true)
    List<Note> getAllByUser(@Param("id") Long id);

    @Query(value = "Select * from notes where title like :title and user_id =:id limit 1", nativeQuery = true)
    Optional<Note> verifyNote(@Param("title") String title, @Param("id") Long id);
}
