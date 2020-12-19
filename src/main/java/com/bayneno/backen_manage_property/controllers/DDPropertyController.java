package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.response.xml.ListDataXmlRequest;
import com.bayneno.backen_manage_property.services.ListingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/xml")
public class DDPropertyController {

    private final XmlMapper xmlMapper;
    private final ListingService listingService;

    public DDPropertyController(XmlMapper xmlMapper
            , ListingService listingService) {
        this.xmlMapper = xmlMapper;
        this.listingService = listingService;
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    String all() throws JsonProcessingException {
        return xmlMapper.writeValueAsString(ListDataXmlRequest.builder()
                .property(listingService.findPublish())
                .build());
    }
}
