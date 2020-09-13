package com.bayneno.backen_manage_property.payload.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {

    String id;

    String name;

    String path;

    Boolean flag = true;
}
