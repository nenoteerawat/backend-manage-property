package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.ChangeLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChangeLogRepository extends MongoRepository<ChangeLog, String> {
    List<ChangeLog> findAllByState(String state);
}
