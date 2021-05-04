package com.finwin.travancore.traviz.home.contact_us.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("Table")
@Expose
private List<ContactsData> table = null;

public List<ContactsData> getTable() {
return table;
}

public void setTable(List<ContactsData> table) {
this.table = table;
}

}