package com.bayneno.backen_manage_property.payload.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {

    private String id;
    private String name;
    private String path;
    private String highlight;
    private String fileDefault;
    private Boolean flag = true;
}
