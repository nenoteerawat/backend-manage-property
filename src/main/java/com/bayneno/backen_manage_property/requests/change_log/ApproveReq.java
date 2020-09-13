package com.bayneno.backen_manage_property.requests.change_log;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveReq {
    private String changeLogId;
    private boolean isApprove;
}
