package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoXml {

    @JsonProperty("picture-url")
    private String pictureUrl;

    @JsonProperty("picture-caption")
    private String pictureCaption;
}
