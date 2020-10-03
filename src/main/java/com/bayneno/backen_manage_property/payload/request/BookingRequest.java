package com.bayneno.backen_manage_property.payload.request;

import com.bayneno.backen_manage_property.models.User;
import com.bayneno.backen_manage_property.payload.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private String listingId;

    private String leadId;

    private String saleUsername;
}
