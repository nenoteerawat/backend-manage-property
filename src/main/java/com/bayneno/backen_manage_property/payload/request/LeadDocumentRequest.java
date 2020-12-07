package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadDocumentRequest {
    String leadId;
    String type;
    Map<String, String> data;
}
