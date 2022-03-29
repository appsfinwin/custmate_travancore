package com.finwin.travancore.traviz.forgot_password;


import com.finwin.travancore.traviz.forgot_password.pojo.ResetPasswordResponse;

public class ForgotPasswordAction {

    public static final int DEFAULT=-1;
    public static final int API_ERROR=2;
    public static final int SENT_OTP_SUCCESS=3;
    public static final int RESENT_OTP_SUCCESS=4;
    public static final int RESET_PASSWORD_SUCCESS=5;

    public int action;
    public String error;
    public ResetPasswordResponse resetPasswordResponse;

    public ForgotPasswordAction(int action, String error) {
        this.action = action;
        this.error = error;
    }

    public ForgotPasswordAction(int action, ResetPasswordResponse resetPasswordResponse) {
        this.action = action;
        this.resetPasswordResponse = resetPasswordResponse;
    }

    public ForgotPasswordAction(int action) {
        this.action = action;
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

    public ResetPasswordResponse getResetPasswordResponse() {
        return resetPasswordResponse;
    }

    public void setResetPasswordResponse(ResetPasswordResponse resetPasswordResponse) {
        this.resetPasswordResponse = resetPasswordResponse;
    }
}
