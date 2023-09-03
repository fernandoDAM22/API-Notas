package com.notes.notes.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.EnableMBeanExport;

@Entity
@Table(name = "notes")
@Data
public class Note {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "category_id")
    private Long categoryId;
}
