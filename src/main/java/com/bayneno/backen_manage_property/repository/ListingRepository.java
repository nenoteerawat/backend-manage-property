package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ListingRepository extends MongoRepository<Listing, String> {

}