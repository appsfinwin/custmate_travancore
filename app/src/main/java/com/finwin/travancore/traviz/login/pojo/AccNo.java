package com.finwin.travancore.traviz.login.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccNo {

    @SerializedName("accNo")
    @Expose
    private String accNo;
    @SerializedName("schname")
    @Expose
    private String schname;

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getSchname() {
        return schname;
    }

    public void setSchname(String schname) {
        this.schname = schname;
    }

}