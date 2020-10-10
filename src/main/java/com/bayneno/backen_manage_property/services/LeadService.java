package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;

import java.util.List;

public interface LeadService {

    String createLead(LeadRequest leadRequest, User user);

//    List<LeadResponse> getLead(LeadSearchRequest listingSearchRequest);

}
