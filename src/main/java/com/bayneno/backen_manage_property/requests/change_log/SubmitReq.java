package com.bayneno.backen_manage_property.requests.change_log;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReq {
    private String id;
    private String type;
    private String submitType;
    private String comment;
    private Object toValue;
}
