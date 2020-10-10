package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.enums.ESubmitTypeChangeLog;
import com.bayneno.backen_manage_property.enums.ETypeChangeLog;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "change_log")
public class ChangeLog {
    @Id
    private String id;
    private ETypeChangeLog type;
    private String typeId;
    private String comment;
    private ESubmitTypeChangeLog submitType;
    @DBRef
    private User submitBy;
    @DBRef
    private User approveBy;
    private String state;
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;
    private ZonedDateTime approveDate;
    private Object fromValue;
    private Object toValue;
}
