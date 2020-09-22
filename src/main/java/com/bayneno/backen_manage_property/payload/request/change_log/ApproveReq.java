package com.bayneno.backen_manage_property.payload.request.change_log;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveReq {
    private String changeLogId;
    private Boolean isApprove;
}
