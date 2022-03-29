package com.finwin.travancore.traviz.forgot_password.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse {

@SerializedName("status")
@Expose
private String status;
@SerializedName("Return_Status")
@Expose
private String returnStatus;
@SerializedName("Return_Id")
@Expose
private String returnId;
@SerializedName("Return_message")
@Expose
private String returnMessage;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getReturnStatus() {
return returnStatus;
}

public void setReturnStatus(String returnStatus) {
this.returnStatus = returnStatus;
}

public String getReturnId() {
return returnId;
}

public void setReturnId(String returnId) {
this.returnId = returnId;
}

public String getReturnMessage() {
return returnMessage;
}

public void setReturnMessage(String returnMessage) {
this.returnMessage = returnMessage;
}

}