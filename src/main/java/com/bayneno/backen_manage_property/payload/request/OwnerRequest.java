package com.bayneno.backen_manage_property.payload.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRequest {

    private String listingCode;
    private String name;
    private String line;
    private String phone;
    private String email;
    private String listingCodeManual;
    private String linkGoogleMap;

}
