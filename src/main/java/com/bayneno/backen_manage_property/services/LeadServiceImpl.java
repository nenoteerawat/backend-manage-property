package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.models.Lead;
import com.bayneno.backen_manage_property.payload.request.LeadRequest;
import com.bayneno.backen_manage_property.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class LeadServiceImpl implements LeadService  {

	@Autowired
	LeadRepository leadRepository;

	@Override
	public String createLead(LeadRequest leadRequest) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		Lead lead = leadRepository.save(
				Lead
						.builder()
						.owner(leadRequest.getOwnerRequest())
						.room(leadRequest.getRoomRequest())
						.fileIds(leadRequest.getFileIds())
						.createdBy(leadRequest.getUsername())
						.createdDateTime(formatter.format(date))
						.build()
		);
		return lead.getId();
	}
}
