package com.bayneno.backen_manage_property.requests.change_log;

import lombok.*;

import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReq {
    private String room;
    private String comment;
    private Map<String, String> toValue;
}
