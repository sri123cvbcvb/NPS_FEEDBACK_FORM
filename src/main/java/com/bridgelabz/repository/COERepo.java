package com.bridgelabz.repository;

import com.bridgelabz.entity.CenterOfExcellence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface COERepo extends JpaRepository<CenterOfExcellence, Long> {

   Optional<CenterOfExcellence> findByName(String name);
}
