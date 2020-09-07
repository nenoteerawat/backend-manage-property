package com.bayneno.backen_manage_property.requests.room;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddReq {
    private String name;
    private String price;
}
