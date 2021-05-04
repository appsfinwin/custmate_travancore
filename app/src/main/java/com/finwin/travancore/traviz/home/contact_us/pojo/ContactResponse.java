package com.finwin.travancore.traviz.home.contact_us.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactResponse {

@SerializedName("data")
@Expose
private Data data;

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

}