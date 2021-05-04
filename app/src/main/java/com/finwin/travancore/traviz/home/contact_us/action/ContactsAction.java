package com.finwin.travancore.traviz.home.contact_us.action;

import com.finwin.travancore.traviz.home.contact_us.pojo.ContactResponse;

public class ContactsAction {
    public static final int DEFAULT=-1;
    public static final int GET_CONTAC_SUCCESS=1;
    public static final int API_ERROR=2;

    ContactResponse contactResponse;
    int action;
    String error;

    public ContactsAction(int action, String error) {
        this.action = action;
        this.error = error;
    }

    public ContactsAction( int action,ContactResponse contactResponse) {
        this.contactResponse = contactResponse;
        this.action = action;
    }

    public ContactsAction(int action) {
        this.action = action;
    }

    public ContactResponse getContactResponse() {
        return contactResponse;
    }

    public void setContactResponse(ContactResponse contactResponse) {
        this.contactResponse = contactResponse;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
