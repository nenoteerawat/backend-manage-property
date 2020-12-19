package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Listing;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.ListingRequest;
import com.bayneno.backen_manage_property.payload.request.ListingSearchRequest;
import com.bayneno.backen_manage_property.payload.request.MatchListingRequest;
import com.bayneno.backen_manage_property.payload.response.ListingResponse;
import com.bayneno.backen_manage_property.payload.response.xml.PropertyXml;

import java.util.List;

public interface ListingService {

    String createListing(ListingRequest listingRequest, User user);

    List<ListingResponse> getListing(ListingSearchRequest listingSearchRequest);

    String editListing(ListingRequest listingRequest, User user);

    List<Listing> matchListing(MatchListingRequest matchListingRequest);

    List<ListingResponse> getListingByAppointment(String leadId, User user);

    List<PropertyXml> findPublish();

}
