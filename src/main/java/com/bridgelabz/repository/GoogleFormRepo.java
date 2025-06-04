package com.bridgelabz.repository;

import com.bridgelabz.entity.Batch;
import com.bridgelabz.entity.GoogleForm;
import com.bridgelabz.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleFormRepo extends JpaRepository<GoogleForm, Long> {

   // Optional<GoogleForm> findByBatch(Batch batch);

    Optional<GoogleForm> findByTechStack(TechStack techStack);
}
