package com.bayneno.backen_manage_property.models;

import com.bayneno.backen_manage_property.payload.response.FileResponse;
import com.bayneno.backen_manage_property.services.NonInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "lead")
public class Lead {

  @NonInfo
  @Id
  private String id;

  @NonInfo
  @DBRef
  private Listing listingByLead;
  private String buildingListingByLead;
  private String propertyTypeListingByLead;
  private String toiletListingByLead;
  private String bedListingByLead;
  private Double areaListingByLead;
  private String floorListingByLead;
  private String directionListingByLead;
  private String zoneListingByLead;
  private String listingByLeadNotes;
  @NonInfo
  @DBRef
  private Listing listingByAdmin;
  private String buildingListingByAdmin;
  private String propertyTypeListingByAdmin;
  private String toiletListingByAdmin;
  private String bedListingByAdmin;
  private Double areaListingByAdmin;
  private String floorListingByAdmin;
  private String directionListingByAdmin;
  private String zoneListingByAdmin;
  private String listingByAdminNotes;
  @NonInfo
  @DBRef
  private Listing listingBySale;
  private String buildingListingBySale;
  private String propertyTypeListingBySale;
  private String toiletListingBySale;
  private String bedListingBySale;
  private Double areaListingBySale;
  private String floorListingBySale;
  private String directionListingBySale;
  private String zoneListingBySale;
  private String listingBySaleNotes;
  @NonInfo
  @DBRef
  private Listing listingLifeStyleBySale;
  private String buildingListingLifeStyleBySale;
  private String propertyTypeListingLifeStyleBySale;
  private String toiletListingLifeStyleBySale;
  private String bedListingLifeStyleBySale;
  private Double areaListingLifeStyleBySale;
  private String floorListingLifeStyleBySale;
  private String directionListingLifeStyleBySale;
  private String zoneListingLifeStyleBySale;
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

  @NonInfo
  private FileResponse file;
  @NonInfo
  private List<FileResponse> books;
  @NonInfo
  private Map<String , String> leasePDF;
  @NonInfo
  private Map<String , String> sellPDF;
  @NonInfo
  private Map<String , String> agentAgreementPDF;
  @NonInfo
  private Map<String , String> exclusivePDF;
  @NonInfo
  private Map<String , String> coBrokePDF;

  @DBRef
  private User saleUser;

  @NonInfo
  @DBRef
  private User createdBy;
  @NonInfo
  private ZonedDateTime createdDateTime;
  @NonInfo
  @DBRef
  private User updatedBy;
  @NonInfo
  private ZonedDateTime updatedDateTime;
}
