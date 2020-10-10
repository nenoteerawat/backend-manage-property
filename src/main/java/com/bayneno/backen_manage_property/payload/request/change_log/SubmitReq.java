package com.bayneno.backen_manage_property.payload.request.change_log;

import com.bayneno.backen_manage_property.enums.ESubmitTypeChangeLog;
import com.bayneno.backen_manage_property.enums.ETypeChangeLog;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReq {
    private String id;
    private ETypeChangeLog type;
    private ESubmitTypeChangeLog submitType;
    private String comment;
    private Object toValue;
    private String username;
}
