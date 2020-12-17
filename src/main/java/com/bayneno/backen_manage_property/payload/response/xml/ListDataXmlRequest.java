package com.bayneno.backen_manage_property.payload.response.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "listing-data")
public class ListDataXmlRequest {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PropertyXml> property = new ArrayList<>();
}
