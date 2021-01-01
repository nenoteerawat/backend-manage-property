package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;

public interface LeadService {

    String createLead(LeadRequest leadRequest, User user);

    String editLead(LeadRequest leadRequest, User user);

//    List<LeadResponse> getLead(LeadSearchRequest listingSearchRequest);
    int calculateFillFieldValuePercentage(Lead lead);

}
