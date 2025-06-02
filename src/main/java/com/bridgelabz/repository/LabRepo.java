package com.bridgelabz.repository;

import com.bridgelabz.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabRepo extends JpaRepository<Lab,Long> {

    Optional<Lab> findByName(String name);
}
