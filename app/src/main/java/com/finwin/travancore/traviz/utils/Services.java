package com.finwin.travancore.traviz.utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Services {

    public static SweetAlertDialog showProgressDialog(Context context) {


        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText("Please wait")
                .show();

        return sweetAlertDialog;
    }
}
