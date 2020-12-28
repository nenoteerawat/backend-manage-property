package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyXml {

    @JsonProperty("external-id")
    private String externalId;

    @JsonProperty("ref-no")
    private String refNo;

    @JsonProperty("ref-id")
    private String refId;

    private LocationXml location;

    private DetailsXml details;

    @JsonProperty("listing-type")
    private String listingType;

    @JsonProperty("agent-id")
    private String agentId;

    @JsonProperty("custom-name")
    private String customName;

    @JsonProperty("custom-phone")
    private String customPhone;

    @JsonProperty("custom-mobile")
    private String customMobile;

    private String tenure;

    private String sold;

    private String status;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PhotoXml> photo = new ArrayList<>();
}
