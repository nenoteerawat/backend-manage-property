package com.bayneno.backen_manage_property.repository;

import com.bayneno.backen_manage_property.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ListingRepository extends MongoRepository<Listing, String> {

    List<Listing> findAllBySaleUser(String saleUser);

    List<Listing> findAllByFlag(boolean flag);
}