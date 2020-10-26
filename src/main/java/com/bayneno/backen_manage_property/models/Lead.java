package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.request.LeadListingRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "lead")
public class Lead {

  @Id
  private String id;

  @DBRef
  private Listing listingByLead;
  private String buildingListingByLead;
  private String propertyTypeListingByLead;
  private String toiletListingByLead;
  private String bedListingByLead;
  private Double areaListingByLead;
  private String floorListingByLead;
  private String directionListingByLead;
  private String listingByLeadNotes;
  @DBRef
  private Listing listingByAdmin;
  private String buildingListingByAdmin;
  private String propertyTypeListingByAdmin;
  private String toiletListingByAdmin;
  private String bedListingByAdmin;
  private Double areaListingByAdmin;
  private String floorListingByAdmin;
  private String directionListingByAdmin;
  private String listingByAdminNotes;
  @DBRef
  private Listing listingBySale;
  private String buildingListingBySale;
  private String propertyTypeListingBySale;
  private String toiletListingBySale;
  private String bedListingBySale;
  private Double areaListingBySale;
  private String floorListingBySale;
  private String directionListingBySale;
  private String listingBySaleNotes;
  @DBRef
  private Listing listingLifeStyleBySale;
  private String buildingListingLifeStyleBySale;
  private String propertyTypeListingLifeStyleBySale;
  private String toiletListingLifeStyleBySale;
  private String bedListingLifeStyleBySale;
  private Double areaListingLifeStyleBySale;
  private String floorListingLifeStyleBySale;
  private String directionListingLifeStyleBySale;
  private String listingLifeStyleBySaleNotes;

  private List<String> painPoints;
  private List<String> painSales;
  private String grade;
  private String priceMin;
  private String priceMax;
  private boolean typeBuy;
  private boolean typeRent;
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
  private int info;

  private String condition;
  private String contract;
  private String typePay;

  @DBRef
  private User saleUser;

  @DBRef
  private User createdBy;
  private ZonedDateTime createdDateTime;
  @DBRef
  private User updatedBy;
  private ZonedDateTime updatedDateTime;
}
