package com.bayneno.backen_manage_property.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "change_log")
public class ChangeLog {
    @Id
    private String id;

    @DBRef
    private Room room;
    private String comment;
    private String sale;
    private String state;
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;
    private ZonedDateTime approveDate;
    private Map<String, String> fromValue;
    private Map<String, String> toValue;
}
