package com.bayneno.backen_manage_property.payload.response.change_log;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogShowResponse {

    private String id;
    private String type;
    private String comment;
    private String submitType;
    private String submitBy;
    private String approveBy;
    private String createDate;
    private String updateDate;
    private String approveDate;
}
