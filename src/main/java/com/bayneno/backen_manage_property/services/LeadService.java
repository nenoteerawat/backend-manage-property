package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.payload.response.LeadResponse;
import com.bayneno.backen_manage_property.payload.request.LeadSearchRequest;

import java.util.List;

public interface LeadService {

    String createLead(LeadRequest leadRequest);

    List<LeadResponse> getLead(LeadSearchRequest leadSearchRequest);

    String editLead(LeadRequest leadRequest);
}
