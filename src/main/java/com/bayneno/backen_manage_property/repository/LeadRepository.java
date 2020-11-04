package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeadRepository extends MongoRepository<Lead, String> {

    List<Lead> findAllBySaleUserId(String userId);
}
