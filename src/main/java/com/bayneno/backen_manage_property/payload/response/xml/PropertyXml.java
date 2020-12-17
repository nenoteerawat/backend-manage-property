package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyXml {

    @JsonProperty("external-id")
    private String externalId;

    @JsonProperty("ref-id")
    private String refId;

    private LocationXml location;

    private DetailsXml details;
}
