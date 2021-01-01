package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.enums.ERole;
import com.bayneno.backen_manage_property.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
