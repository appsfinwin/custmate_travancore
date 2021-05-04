package com.finwin.travancore.traviz.home.contact_us.adapter;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.home.contact_us.action.ContactsAction;
import com.finwin.travancore.traviz.home.contact_us.pojo.ContactsData;

public class ContacsRowViewModel {
    MutableLiveData<ContactsAction> mAction;
    ContactsData data;
    String name,phoneNumber,designation;

    public ContacsRowViewModel(MutableLiveData<ContactsAction> mAction, ContactsData data) {
        this.mAction = mAction;
        this.data = data;
    }

    public String getName() {
        name= data.getName();
        return name;
    }

    public String getPhoneNumber() {
        phoneNumber=data.getMobileNumber();
        return phoneNumber;
    }

    public String getDesignation() {
        designation= data.getDesignation();
        return designation;
    }

    public void setData(ContactsData contactsData) {
        this.data=contactsData;
    }
}
