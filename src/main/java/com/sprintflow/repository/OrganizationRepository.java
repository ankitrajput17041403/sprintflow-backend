package com.sprintflow.repository;

import com.sprintflow.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Override
    Optional<Organization> findById(Long id);
}
