package com.bayneno.backen_manage_property.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
