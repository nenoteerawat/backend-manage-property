package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SizeDetailsXml {

    @JsonProperty("floor-area")
    private String floorArea;

    @JsonProperty("floor-size-x")
    private String floorSizeX;

    @JsonProperty("floor-size-y")
    private String floorSizeY;

    @JsonProperty("land-area")
    private String landArea;

    @JsonProperty("land-size-x")
    private String landSizeX;

    @JsonProperty("land-size-y")
    private String langSizeY;
}
