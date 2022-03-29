package com.finwin.travancore.traviz.forgot_password;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.ActivityInitial;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.databinding.ActivityForgotPasswordBinding;
import com.finwin.travancore.traviz.forgot_password.reset_password.ResetPasswordActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity {

    ForgotPasswordViewModel viewModel;
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=  DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
       viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
       binding.setViewModel(viewModel);

       viewModel.getmAction().observe(this, forgotPasswordAction ->

       {
           viewModel.cancelLoading();
           switch (forgotPasswordAction.getAction())
           {
               case ForgotPasswordAction.SENT_OTP_SUCCESS:
               {

                   View customView= LayoutInflater.from(this).inflate(R.layout.layout_error_layout,null);
                   TextView tv_error=customView.findViewById(R.id.tv_error);

                   tv_error.setText("OTP sent successfully");
                   //tv_error_message.setText(forgotPasswordAction.getResetPasswordResponse().getReturnMessage());

                   new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sweetAlertDialog) {

                                   Intent intent= new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                   intent.putExtra("otp_id",forgotPasswordAction.getResetPasswordResponse().getReturnId());
                                   intent.putExtra("account_number",viewModel.accountNumber.get());
                                   startActivity(intent);
                                   sweetAlertDialog.cancel();
                                   finish();
                               }
                           })
                           .setTitleText("SUCCESS!")
                           //.setContentText(loginAction.getError())
                           .setCustomView(customView)
                           .show();

               }
               break;

               case ForgotPasswordAction.API_ERROR:
               {
                   View customView= LayoutInflater.from(this).inflate(R.layout.layout_error_layout,null);
                   TextView tv_error=customView.findViewById(R.id.tv_error);
                   tv_error.setText(forgotPasswordAction.getError());

                   new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                           .setTitleText("Error!")
                           //.setContentText(loginAction.getError())
                           .setCustomView(customView)
                           .show();
               }
               break;
           }

       });

    }
    @Override
    public void onBackPressed() {

        exitDialog();
    }

    private void exitDialog() {
        new AlertDialog.Builder(this,R.style.alertDialog)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setCancelable(false)
                .setMessage("Do you want to Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, ActivityInitial.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}