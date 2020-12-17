package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationXml {

    @JsonProperty("streetname")
    private String streetName;

    @JsonProperty("streetnumber")
    private String streetNumber;

    @JsonProperty("post-code")
    private String postCode;

    @JsonProperty("area-code")
    private String areaCode;

    @JsonProperty("district-code")
    private String districtCode;

    @JsonProperty("region-code")
    private String regionCode;

    private String longitude;

    private String latitude;

    @JsonProperty("property-name")
    private String propertyName;

    @JsonProperty("property-id")
    private String propertyId;

    @JsonProperty("property-type-group")
    private String propertyTypeGroup;
}
