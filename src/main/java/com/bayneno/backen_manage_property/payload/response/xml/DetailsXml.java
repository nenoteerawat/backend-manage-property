package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsXml {

    private String title;

    @JsonProperty("title_en")
    private String titleEn;

    private String description;

    @JsonProperty("description_en")
    private String descriptionEn;

    private String features;

    private String amenities;

    @JsonProperty("price-details")
    private PriceDetailsXml priceDetails;
}
