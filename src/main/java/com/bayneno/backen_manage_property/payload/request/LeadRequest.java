package com.bayneno.backen_manage_property.payload.request;

import com.bayneno.backen_manage_property.payload.response.FileResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadRequest {
    private String id;

    @Builder.Default
    private LeadListingRequest listingByLead = LeadListingRequest.builder().build();
    @Builder.Default
    private LeadListingRequest listingByAdmin = LeadListingRequest.builder().build();
    @Builder.Default
    private LeadListingRequest listingBySale = LeadListingRequest.builder().build();
    @Builder.Default
    private LeadListingRequest listingLifeStyleBySale = LeadListingRequest.builder().build();

    private boolean typeBuy;
    private boolean typeRent;
    @Builder.Default
    private List<String> painPoints = new ArrayList<>();
    @Builder.Default
    private List<String> paintSales = new ArrayList<>();
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

    private String comment;
}
