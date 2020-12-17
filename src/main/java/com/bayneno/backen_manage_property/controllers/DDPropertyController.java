package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.response.xml.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/xml")
public class DDPropertyController {

    private final XmlMapper xmlMapper;

    public DDPropertyController(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    String all() throws IOException {
        return xmlMapper.writeValueAsString(ListDataXmlRequest.builder()
                .property(Stream.of(PropertyXml.builder()
                        .externalId(UUID.randomUUID().toString())
                        .agentId(UUID.randomUUID().toString())
                        .customMobile("0000000000")
                        .customName("Customer")
                        .customPhone("00000000")
                        .details(DetailsXml.builder()
                                .amenities("abc")
                                .description("abcd")
                                .descriptionEn("abcdef")
                                .facing("aaaaa")
                                .features("ffffff")
                                .floorLevel("1")
                                .furnishing("dfdfdf")
                                .numberOfFloors("2")
                                .parkingSpaces("123")
                                .priceDetails(PriceDetailsXml.builder()
                                        .currencyCode("sdofkewf")
                                        .price("10000")
                                        .priceDescription("sererwefw")
                                        .priceType("efcdfs")
                                        .priceUnit("12")
                                        .build())
                                .room(RoomXml.builder()
                                        .extraRooms("serewr")
                                        .numBathrooms("1")
                                        .numBedrooms("2")
                                        .build())
                                .sizeDetails(SizeDetailsXml.builder()
                                        .floorArea("12")
                                        .floorSizeX("11")
                                        .floorSizeY("14")
                                        .landArea("34")
                                        .build())
                                .title("serewsr")
                                .titleEn("ewrwerw")
                                .build())
                        .listingType("serfwe")
                        .location(LocationXml.builder()
                                .areaCode("seresr")
                                .districtCode("serser")
                                .latitude("12213")
                                .longitude("232323")
                                .postCode("2312341")
                                .propertyId("12343245")
                                .propertyName("seresef")
                                .propertyTypeGroup("sffesfe")
                                .regionCode("2312313")
                                .streetName("serser")
                                .streetNumber("12123412")
                                .build())
                        .photo(Stream.of(PhotoXml.builder()
                                .pictureCaption("serserrswrer")
                                .pictureUrl("suerewofenmwfwekf")
                                .build()).collect(Collectors.toList()))
                        .refId("2132231231")
                        .sold("213sdfse")
                        .status("ACTIVE")
                        .tenure("aserse")
                        .build()).collect(Collectors.toList()))
                .build());
    }
}
