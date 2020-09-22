package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequest {

    private File file;

    private String name;
    private String path;
    private String highlight;
    private String fileDefault;
}
