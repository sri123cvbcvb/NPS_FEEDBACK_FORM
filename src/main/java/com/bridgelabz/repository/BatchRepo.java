package com.bridgelabz.repository;

import com.bridgelabz.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchRepo extends JpaRepository<Batch, Long> {

    Optional<Batch> findByNameAndLabIdAndCenterId(String name, Long labId, Long coeId);
}
