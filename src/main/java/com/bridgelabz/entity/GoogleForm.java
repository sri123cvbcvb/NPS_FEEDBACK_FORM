package com.bridgelabz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formLink;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @ManyToOne
    private TechStack techStack;

}

