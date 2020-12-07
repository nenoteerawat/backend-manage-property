package com.bayneno.backen_manage_property.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "pdf-default-parameters")
public class PdfDefaultParameters {
    private Map<String, String> leaseAgreement = new HashMap<>();
    private Map<String, String> realEstateAgentAgreement = new HashMap<>();
    private Map<String, String> sellAndPurchaseAgreement = new HashMap<>();
    private Map<String, String> exclusiveAgreement = new HashMap<>();
    private Map<String, String> coBrokerFrom = new HashMap<>();
}
