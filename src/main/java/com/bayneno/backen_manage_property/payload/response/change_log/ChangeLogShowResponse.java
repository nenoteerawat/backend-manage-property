package com.bayneno.backen_manage_property.payload.response.change_log;

import lombok.*;

import java.time.ZonedDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogShowResponse {

    private String id;
    private String type;
    private String comment;
    private String submitBy;
    private String approveBy;
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;
    private ZonedDateTime approveDate;
}
