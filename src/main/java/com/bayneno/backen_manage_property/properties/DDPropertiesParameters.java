package com.bayneno.backen_manage_property.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "dd-properties")
public class DDPropertiesParameters {
    private Map<String, String> status = new HashMap<>();
    private Map<String, String> sold = new HashMap<>();
    private Map<String, String> tenure = new HashMap<>();
    private Map<String, String> features = new HashMap<>();
    private Map<String, String> furnishing = new HashMap<>();
    private Map<String, String> facing = new HashMap<>();
    private Map<String, String> amenities = new HashMap<>();

}
