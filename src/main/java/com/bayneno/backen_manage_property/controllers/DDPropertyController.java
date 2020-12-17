package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.payload.response.xml.ListDataXmlRequest;
import com.bayneno.backen_manage_property.payload.response.xml.PropertyXml;
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
                        .build()).collect(Collectors.toList()))
                .build());
    }
}
