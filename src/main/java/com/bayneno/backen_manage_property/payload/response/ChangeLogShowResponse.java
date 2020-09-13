package com.bayneno.backen_manage_property.payload.response;

import lombok.*;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogShowResponse {
    private String key;
    private String fromValue;
    private String toValue;
}
