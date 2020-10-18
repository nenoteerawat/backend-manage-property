package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.ActionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface ActionLogRepository extends MongoRepository<ActionLog, String> {

    List<ActionLog> findByListingId(String listingId);

    List<ActionLog> findAllBySaleIdOrderByActionDateTimeDesc(String username);

}
