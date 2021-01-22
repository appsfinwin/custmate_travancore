package com.finwin.travancore.traviz.home.transfer.transfer_account_otp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.FundTransferReceipt;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FundTransferAccOTP extends AppCompatActivity {

    Enc_crypter encr = new Enc_crypter();
    SweetAlertDialog sweetDialog,dialog;

    Button btn_SendOTP;
    EditText edt_OTP;
    TextView tv_ReSendOTP;
    String receiptTransferID, receiptTransferDate, receiptDebitAccno, receiptCreditAccno,
            receiptName, receiptMobile, receiptOldBalance, receiptWithdrawalAmount, receiptCurrentBalance,
            Str_OTP = "",
            StrAccNo, StrCrAccNo, StrProcess_amnt, Str_agent_id, Str_otp_data, Str_otp_id,
            StrOTP_data, StrOTP_id,from,TXN_PAYMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    String  msg, beneid;
    private static final String TAG = "FundTransferAccOTP";
    FundTranferOtpViewmodel viewmodel;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_fund_transfer_otpview);
        sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        sharedPreferences= getSharedPreferences(" com.finwin.travancore.traviz",Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        viewmodel=new ViewModelProvider((ViewModelStoreOwner) this).get(FundTranferOtpViewmodel.class);
        Intent intent = getIntent();
        from=intent.getStringExtra("from");
        StrAccNo = intent.getStringExtra("account_number");
        StrCrAccNo = intent.getStringExtra("cr_account_no");
        StrProcess_amnt = intent.getStringExtra("process_amount");
        Str_agent_id = intent.getStringExtra("agent_id");
        Str_otp_data = intent.getStringExtra("otp_data");
        Str_otp_id = intent.getStringExtra("otp_id");
        beneid=intent.getStringExtra("ben_id");
        TXN_PAYMODE=intent.getStringExtra("TXN_PAYMODE");


        viewmodel.getmAction().observe(this, new Observer<FundTransferOtpAction>() {
            @Override
            public void onChanged(FundTransferOtpAction fundTransferOtpAction) {

               cancelLoading();
                switch (fundTransferOtpAction.getAction())
                {
                    case FundTransferOtpAction.NEFT_SUCCESS:

                    case FundTransferOtpAction.ACCOUNT_TRANSFER_SUCCESS:


                        receiptTransferID = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getTranId();
                        receiptTransferDate = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getTransferDate();
                        receiptDebitAccno = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getAccNo();
                        receiptCreditAccno = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getBenAccNo();
                        receiptName = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getName();
                        receiptMobile = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getMobile();
                        receiptOldBalance = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getOldBalance();
                        receiptWithdrawalAmount = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getWithdrawalAmount();
                        receiptCurrentBalance = fundTransferOtpAction.getNeftTransferResponse().getReceipt().getData().getCurrentBalance();


                        Intent intent = new Intent(FundTransferAccOTP.this, FundTransferReceipt.class);
                        intent.putExtra("transactionID", receiptTransferID);
                        intent.putExtra("transactionDate", receiptTransferDate);
                        intent.putExtra("account_number", receiptDebitAccno);
                        intent.putExtra("debit_account_number", receiptCreditAccno);
                        intent.putExtra("name", receiptName);
                        intent.putExtra("mobile", receiptMobile);
                        intent.putExtra("old_balance", receiptOldBalance);
                        intent.putExtra("withdrawalAmount", receiptWithdrawalAmount);
                        intent.putExtra("current_balance", receiptCurrentBalance);
                        startActivity(intent);
                        finish();

                        break;
                        case FundTransferOtpAction.API_ERROR:

                            new SweetAlertDialog(FundTransferAccOTP.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText(fundTransferOtpAction.getError())
                                    .setTitleText("Error")
                                    .show();
                            break;

                            case FundTransferOtpAction.OTP_SUCCESS:

                                edt_OTP.setText("");
                                StrOTP_data = fundTransferOtpAction.getResendOtpResponse().getOtp().getData();
                                Str_otp_id =  fundTransferOtpAction.getResendOtpResponse().getOtp().getOtpId();
                                new SweetAlertDialog(FundTransferAccOTP.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setContentText("OTP sent successfully!")
                                        .setTitleText("Success")
                                        .show();
                                break;


                }
            }
        });

        edt_OTP = findViewById(R.id.edt_trnsfr_otp);
        tv_ReSendOTP = findViewById(R.id.tv_resend_otp);
        btn_SendOTP = findViewById(R.id.btn_trnsfr_snd_otp);

        btn_SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_OTP.getText().toString().equals("")) {
                    Toast.makeText(FundTransferAccOTP.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {

                    Str_OTP = edt_OTP.getText().toString();

                    if (from.equals("neft")){
                       // transfer_neft();
                        neftApiCall();
                    }else if (from.equals("account")) {
                        //Transfer();
                        accountTransfer();
                    }
                }
            }
        });

        tv_ReSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });

    }

    public void neftApiCall(){

        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.setContentText("Transferring..").show();

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();

        items.put("ben_id", beneid);
        items.put("otp_val", Str_OTP);
        items.put("otp_id", Str_otp_id);
        items.put("account_no", StrAccNo);
        items.put("beni_account_no", StrCrAccNo);
        items.put("process_amount", StrProcess_amnt);
        items.put("agent_id", Str_agent_id);
        items.put("transferType", "own");
        items.put("TXN_PAYMODE", TXN_PAYMODE);

        String request=(new JSONObject(items)).toString();
        Log.d(TAG, "neft: "+request);

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        String request1=(new JSONObject(params)).toString();
        viewmodel.neftApi(params);
    }

    public void  accountTransfer()
    {
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.setContentText("Transferring..").show();

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();

        items.put("otp_val", Str_OTP);
        items.put("otp_id", Str_otp_id);
        items.put("account_no", StrAccNo);
        items.put("beni_account_no", StrCrAccNo);
        // beni
        items.put("process_amount", StrProcess_amnt);
        items.put("agent_id", Str_agent_id);
        items.put("transferType", "own");
        //
//                params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        String request=(new JSONObject(items)).toString();
        Log.d(TAG, "transfer_request: "+request);
        String data = encr.conRevString(Enc_Utils.enValues(items));
        Log.e("getParams: data =", data);
        params.put("data", data);
        String request1=(new JSONObject(params)).toString();
        Log.d(TAG, "transfer_request: "+request);
        viewmodel.accountTransfer(params);
    }


    public void resendOtp(){
        sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetDialog.setTitleText("Requesting OTP");
        sweetDialog.setCancelable(false);
        sweetDialog.show();

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("particular", "mob_resent");
        items.put("account_no", ConstantClass.const_accountNumber);
        items.put("amount", StrProcess_amnt);
        items.put("agent_id", ConstantClass.const_cusId);



        String request=(new JSONObject(items)).toString();
        Log.d(TAG, "transfer_request: "+request);
        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        viewmodel.resendOtp(params);

    }

    public void cancelLoading()
    {
        if(dialog!=null)
        {
            dialog.dismiss();
        }

        if(sweetDialog!=null)
        {
            sweetDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            editor.putString("success","no");
            editor.commit();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Click again to cancel the payment", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}