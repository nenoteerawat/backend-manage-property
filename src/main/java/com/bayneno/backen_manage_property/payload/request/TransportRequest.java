package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransportRequest {

    private String type;
    private String name;
    private int range;

    @Override
    public String toString() {
        return "(" + type + ", " + name + ", " + range + " ม.)";
    }
}
