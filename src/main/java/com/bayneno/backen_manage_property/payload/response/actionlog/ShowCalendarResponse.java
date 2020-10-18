package com.bayneno.backen_manage_property.payload.response.actionlog;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowCalendarResponse {
    private String id;
    private String title;
    private String start;
    private String color;
    private String done;
}
