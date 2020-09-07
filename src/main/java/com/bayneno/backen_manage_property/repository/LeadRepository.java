package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeadRepository extends MongoRepository<Lead, String> {

}