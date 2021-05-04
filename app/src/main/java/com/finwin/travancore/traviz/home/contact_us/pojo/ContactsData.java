package com.finwin.travancore.traviz.home.contact_us.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactsData {

@SerializedName("Branch Code")
@Expose
private String branchCode;
@SerializedName("Branch Name")
@Expose
private String branchName;
@SerializedName("Name")
@Expose
private String name;
@SerializedName("Designation")
@Expose
private String designation;
@SerializedName("Mobile Number")
@Expose
private String mobileNumber;

public String getBranchCode() {
return branchCode;
}

public void setBranchCode(String branchCode) {
this.branchCode = branchCode;
}

public String getBranchName() {
return branchName;
}

public void setBranchName(String branchName) {
this.branchName = branchName;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDesignation() {
return designation;
}

public void setDesignation(String designation) {
this.designation = designation;
}

public String getMobileNumber() {
return mobileNumber;
}

public void setMobileNumber(String mobileNumber) {
this.mobileNumber = mobileNumber;
}

}