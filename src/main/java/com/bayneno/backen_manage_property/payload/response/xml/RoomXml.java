package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomXml {

    @JsonProperty("num-bedrooms")
    private String numBedrooms;

    @JsonProperty("extra-rooms")
    private String extraRooms;

    @JsonProperty("num-bathrooms")
    private String numBathrooms;
}
