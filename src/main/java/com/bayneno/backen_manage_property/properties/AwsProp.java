package com.bayneno.backen_manage_property.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "aws")
public class AwsProp {
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
