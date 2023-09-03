package com.notes.notes.repository;

import com.notes.notes.entity.Note;
import com.notes.notes.entity.User;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRespository extends JpaRepository<User,Long> {
    @Query(value = "Select * from users where name like :name limit 1",nativeQuery = true)
    Optional<User> findByName(@Param("name") String name);
    @Query(value = "Select * from users where email like :email limit 1", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
    @Query(value = "Select * from notes where id = :id",nativeQuery = true)
    List<Note> getAllNotes(@Param("id") Long id);

}
