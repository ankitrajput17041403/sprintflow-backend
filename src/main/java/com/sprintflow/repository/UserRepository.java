package com.sprintflow.repository;

import com.sprintflow.entity.Organization;
import com.sprintflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Organization,Long> {

    Optional<User> findByEmail(String email);


}

