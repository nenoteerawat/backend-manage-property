package com.bayneno.backen_manage_property.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "field_object_map")
public class FieldObjectMap {
    @Id
    private String id;
    private String fieldName;
    private String type;
    private String th;
    private String en;
}
