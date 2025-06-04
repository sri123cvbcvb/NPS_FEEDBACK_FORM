package com.bridgelabz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Lab lab;

    @ManyToOne
    private CenterOfExcellence center;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<TechStack> techStackList;

}
