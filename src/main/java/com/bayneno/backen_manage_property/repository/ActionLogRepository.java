package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.ActionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ActionLogRepository extends MongoRepository<ActionLog, String> {

    List<ActionLog> findByListingId(String listingId);

    List<ActionLog> findAllByActionDateTimeAndCreatedByIdOrderByActionDateTimeDesc(String date, String username);
    List<ActionLog> findAllByLeadIdAndCreatedByIdOrderByActionDateTimeDesc(String leadId, String username);


}
