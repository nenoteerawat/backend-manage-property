package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Optional<Project> findByName(String projectName);
}
