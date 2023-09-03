package com.notes.notes.repository;

import com.notes.notes.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "Select * from categories where name like :name", nativeQuery = true)
    Optional<Category> findByName(@Param("name") String name);
    @Query(value = "Select * from categories where name = :name and user_id = :id", nativeQuery = true)
    Category getUserCategory(@Param("name") String name, @Param("id")Long id);
    @Query(value = "Select * from categories where user_id = :id", nativeQuery = true)
    List<Category> getAllByUser(@Param("id") Long id);
}
