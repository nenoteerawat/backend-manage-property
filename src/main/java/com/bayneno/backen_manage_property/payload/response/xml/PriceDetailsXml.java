package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriceDetailsXml {

    private String price;

    @JsonProperty("price-unit")
    private String priceUnit;

    @JsonProperty("price-description")
    private String priceDescription;

    @JsonProperty("price-type")
    private String priceType;

    @JsonProperty("currency-code")
    private String currencyCode;

    private DetailsXml details;
}