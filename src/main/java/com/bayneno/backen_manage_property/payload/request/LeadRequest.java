package com.bayneno.backen_manage_property.payload.request;

import com.bayneno.backen_manage_property.payload.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeadRequest {
    private String id;

    private LeadListingRequest listingByLead;
    private LeadListingRequest listingByAdmin;
    private LeadListingRequest listingBySale;
    private LeadListingRequest listingLifeStyleBySale;

    private boolean typeBuy;
    private boolean typeRent;
    private List<String> painPoints;
    private List<String> paintSales;
    private String grade;
    private String priceMin;
    private String priceMax;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String line;
    private String phone;
    private String job;
    private String reason;
    private String address;
    private String district;
    private String amphoe;
    private String province;
    private String zipcode;
    private String nationality;
    private String career;
    private String yearOfWork;
    private String incomeType;
    private String averageIncome;
    private String liabilities;
    private String billingAmount;
    private String preApprove;
    private String status;
    private int difficulty;
    private int rapport;

    private String saleUser;

    private String condition;
    private String contract;
    private String typePay;

    private FileResponse file;
}
