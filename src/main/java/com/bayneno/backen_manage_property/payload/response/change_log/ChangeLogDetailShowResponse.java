package com.bayneno.backen_manage_property.payload.response.change_log;

import lombok.*;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogDetailShowResponse {
    private String key;
    private String fromValue;
    private String toValue;
}
