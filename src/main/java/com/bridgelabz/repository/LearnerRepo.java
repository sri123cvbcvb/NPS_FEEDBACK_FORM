package com.bridgelabz.repository;

import com.bridgelabz.entity.Batch;
import com.bridgelabz.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearnerRepo extends JpaRepository<Learner, Long> {


    List<Learner> findByBatch(Batch batch);
}
