package com.finwin.travancore.traviz.forgot_password.reset_password;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.ActivityInitial;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.databinding.ActivityResetPasswordBinding;
import com.finwin.travancore.traviz.forgot_password.ForgotPasswordAction;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ResetPasswordActivity extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        binding.setViewModel(viewModel);

        Intent intent = getIntent();
        viewModel.etOtpId.set(intent.getStringExtra("otp_id"));
        viewModel.accountNumber.set(intent.getStringExtra("account_number"));

        viewModel.setTimer(binding);
        viewModel.getmAction().observe(this, new Observer<ForgotPasswordAction>() {
            @Override
            public void onChanged(ForgotPasswordAction forgotPasswordAction) {

                viewModel.cancelLoading();
                switch (forgotPasswordAction.getAction()) {
                    case ForgotPasswordAction.RESET_PASSWORD_SUCCESS:

                        View customView= LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.layout_error_layout,null);
                        TextView tv_error=customView.findViewById(R.id.tv_error);
                        tv_error.setText("Password changed successfully! please login again");

                        new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        Intent intent= new Intent(ResetPasswordActivity.this, ActivityInitial.class);
                                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .setTitleText("SUCCESS!")
                                //.setContentText(loginAction.getError())
                                .setCustomView(customView)
                                .show();
                        break;

                    case ForgotPasswordAction.API_ERROR:
                    {
                        View customView1= LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.layout_error_layout,null);
                        TextView tv_error1=customView1.findViewById(R.id.tv_error);
                        tv_error1.setText(forgotPasswordAction.getError());

                        new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                //.setContentText(loginAction.getError())
                                .setCustomView(customView1)
                                .show();
                    }
                        break;

                    case ForgotPasswordAction.RESENT_OTP_SUCCESS:
                        viewModel.etOtpId.set(forgotPasswordAction.getResetPasswordResponse().getReturnId());
                        View customView1= LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.layout_error_layout,null);
                        TextView tv_error1=customView1.findViewById(R.id.tv_error);
                        tv_error1.setText("OTP sent successfully");

                        new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS!")
                                //.setContentText(loginAction.getError())
                                .setCustomView(customView1)
                                .show();
                        viewModel.setTimer(binding);

                        break;
                }
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
                        Intent intent = new Intent(ResetPasswordActivity.this, ActivityInitial.class);
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