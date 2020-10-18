package com.bayneno.backen_manage_property.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "action-log-calendar")
public class ActionLogCalendar {
    private Map<String, String> typeMapEventColor;
}
