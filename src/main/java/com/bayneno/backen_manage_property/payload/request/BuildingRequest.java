package com.bayneno.backen_manage_property.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingRequest {

    private String floor;
    private String building;
    private String develop;

    @Override
    public String toString() {
        return "(ชั้น : " + floor + "ชั้น , ตึก : " + building + "ตึก , ปีที่สร้างเสร็จ : " + develop + " ปี)";
    }
}
