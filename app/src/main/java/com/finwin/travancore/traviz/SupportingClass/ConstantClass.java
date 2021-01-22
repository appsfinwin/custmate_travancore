package com.finwin.travancore.traviz.SupportingClass;

import com.finwin.travancore.traviz.login.pojo.AccNo;

import java.util.ArrayList;

public class ConstantClass {

//    public static final String ip_global = "http://192.168.0.221:5363";
//    public static final String ip_global = "http://209.126.76.226:5363";
//    public static final String ip_global = "http://35.196.223.10:5363";
//    public static final String ip_global = "http://192.168.0.223:120";

      public static final String ip_global = "http://www.finwintechnologies.com:5363";
      //public static final String ip_global = "https://custmateuvnl.digicob.in";
    //public static final String ip_global = "http://192.168.225.221:81";
    //public static final String ip_global = "http://192.16.0.200:81";
//
//    public static final String api_url_APIkey = ip_global + "/APIkey";
//    public static final String api_OTPGenerate = ip_global + "/OTPGenerate";
//    public static final String api_updateMPIN = ip_global + "/updateMPIN";
//    public static final String api_registerMPIN = ip_global + "/registerMPIN";
//    public static final String api_custappUserLogin = ip_global + "/custappUserLogin";
//    public static final String api_ValidateMPIN = ip_global + "/ValidateMPIN";
//    public static final String api_getAccountHolder = ip_global + "/getAccountHolder";
//    public static final String api_balanceEnqury = ip_global + "/balanceEnqury";
//    public static final String api_getMiniStatement = ip_global + "/getMiniStatement";
//    public static final String api_cashTransfer = ip_global + "/cashTransfer";
//    public static final String api_getMasters = ip_global + "/getMasters";
//    public static final String api_getOperatorList = ip_global + "/getOperatorList";
//    public static final String api_getCircleList = ip_global + "/getCircleList";
//    public static final String api_RechargeMobile = ip_global + "/RechargeMobile";
//    public static final String api_custCreateBeneficiary = ip_global + "/custCreateBeneficiary";
//    public static final String api_custRequestingTransaction = ip_global + "/custRequestingTransaction";
//    public static final String api_custSndrRegredReceiversList = ip_global + "/custSenderRegisteredReceiversList";
//    public static final String api_custConfirmTrnsctnRqst = ip_global + "/custConfirmTransactionRequest";
//    public static final String api_custSelectBeneficiary = ip_global + "/custSelectBeneficiary";

    public static final String url_news = "https://newsapi.org/v2/top-headlines?sources=medical-news-today&apiKey=880ea11837b94135b565e6cbca5fe20a";
    public static boolean NEWS_FIRSTRUN = true;
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_URL = "url";
    public static final String KEY_URLTOIMAGE = "urlToImage";
    public static final String KEY_PUBLISHEDAT = "publishedAt";


    public static Boolean mpinStatus;
    public static String[] const_acc_nos_array;
    public static ArrayList<String> listAccountNumbers;
    public static ArrayList<String> listScheme;
    public static String const_accountNumber, const_phone, const_name, const_password,const_cusId, const_loan_no = "3253";
    public static final String mstrType = "M_TYPE";
    public static String[] masterTypArray;
    public static String[] masterTypArrayID;

    public static String pinforQR = "qrpass";

    public static final String PREFS_DATA = "PrefesContent";
    //    public static String PREFS_MPIN = "";

//    public static String QR_SCND = "QRSCND";
//    public static String QR_ACCNO = "QRACCNO";

}
