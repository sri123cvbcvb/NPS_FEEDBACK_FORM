package com.bridgelabz.repository;

import com.bridgelabz.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechStackRepo extends JpaRepository<TechStack, Long> {
    Optional<TechStack> findByNameAndBatchId(String techStackName, Long id);

    TechStack findByName(String techStackName);
}
