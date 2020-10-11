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
  private String listingByLeadNotes;
  @DBRef
  private Listing listingByAdmin;
  private String listingByAdminNotes;

  @DBRef
  private Listing listingBySale;
  private String listingBySaleNotes;

  private List<String> painPoints;
  private String grade;
  private String price;
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


  @DBRef
  private User createdBy;
  private ZonedDateTime createdDateTime;
  @DBRef
  private User updatedBy;
  private ZonedDateTime updatedDateTime;
}
