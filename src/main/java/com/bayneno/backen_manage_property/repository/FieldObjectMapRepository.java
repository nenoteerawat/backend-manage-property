package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.FieldObjectMap;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FieldObjectMapRepository extends MongoRepository<FieldObjectMap, String> {
    Optional<FieldObjectMap> findFirstByFieldName(String fieldName);
    List<FieldObjectMap> findAllByType(String type);
}
