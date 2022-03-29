package com.finwin.travancore.traviz.forgot_password.reset_password;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.databinding.ActivityResetPasswordBinding;
import com.finwin.travancore.traviz.forgot_password.ForgotPasswordAction;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.utils.Services;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ResetPasswordViewModel extends AndroidViewModel {

    CompositeDisposable compositeDisposable;
    MutableLiveData<ForgotPasswordAction> mAction;
    Enc_crypter encr = new Enc_crypter();
    ResetPasswordRepository repository= ResetPasswordRepository.getInstance();
    Application application;
    ApiInterface apiInterface;

    public ObservableField<String> etOtp= new ObservableField<>("");
    public ObservableField<String> etOtpId= new ObservableField<>("");
    public ObservableField<String> etPassword= new ObservableField<>("");
    public ObservableField<String> etConfirmPassword= new ObservableField<>("");
    public ObservableField<String> accountNumber= new ObservableField<>("");
    public ObservableField<String> otpTime= new ObservableField<>("");

    public ResetPasswordViewModel(@NonNull @NotNull Application application) {
        super(application);

        this.application=application;
        mAction= new MutableLiveData<>();
        compositeDisposable= new CompositeDisposable();
        repository.setCompositeDisposable(compositeDisposable);
        repository.setmAction(mAction);

    }

    SweetAlertDialog loading;

    public void initLoading(Context context) {
        loading = Services.showProgressDialog(context);
    }

    public void cancelLoading() {
        if (loading != null) {
            loading.cancel();
            loading = null;
        }
    }

    public MutableLiveData<ForgotPasswordAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }

    public void setTimer(ActivityResetPasswordBinding binding)
    {
       Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())

                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here
                        binding.tvOtpTime.setVisibility(View.VISIBLE);
                        otpTime.set("in " +(60 - x) + " seconds");
                        binding.tvResendOtp.setClickable(false);
                        binding.tvResendOtp.setTextColor(application.getResources().getColor(R.color.black_txt));
                    }
                })
                .takeUntil(aLong -> aLong == 60)
                .doOnComplete(new Action() {
                                  @Override
                                  public void run() throws Exception {

                                      binding.tvOtpTime.setVisibility(View.GONE);
                                      binding.tvResendOtp.setClickable(true);
                                      binding.tvResendOtp.setTextColor(application.getResources().getColor(R.color.colorPrimaryDark));
                                  }
                              }

                ).subscribe();


    }


    public void clickResetPassword(View view)
    {
       if ( etOtp.get().equals(""))
       {
           Toast.makeText(view.getContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
       }else if ( etPassword.get().equals(""))
       {
           Toast.makeText(view.getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
       }else if ( etConfirmPassword.get().equals(""))
       {
           Toast.makeText(view.getContext(), "Please confirm password", Toast.LENGTH_SHORT).show();
       }else if ( !etConfirmPassword.get().equals(etPassword.get()))
       {
           Toast.makeText(view.getContext(), "Passwords didn't match", Toast.LENGTH_SHORT).show();
       } else {

           initLoading(view.getContext());
           resetPassword();

       }
    }

    public  void clickResendOtp(View view)
    {
        initLoading(view.getContext());
        resentOtp();

    }
    private void resentOtp() {
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        //items.put("account_no", obAccountNumber.get());
        items.put("Flag", "REQUEST_OTP");
        items.put("MobileNo", "");
        items.put("OTP_ID", "");
        items.put("OTP", "");
        items.put("NewPassword", "");
        items.put("Accountno", accountNumber.get());

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        String request = (new JSONObject(items)).toString();
        String data = encr.conRevString(Enc_Utils.enValues(items));
        params.put("data", data);
        String requestEncrypted = (new JSONObject(params)).toString();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.resentOtp(apiInterface,body);
    }



    private void resetPassword() {

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        //items.put("account_no", obAccountNumber.get());
        items.put("Flag", "UPDATE");
        items.put("MobileNo", "");
        items.put("OTP_ID", etOtpId.get());
        items.put("OTP", etOtp.get());
        items.put("NewPassword", etPassword.get());
        items.put("Accountno", accountNumber.get());

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        String request = (new JSONObject(items)).toString();
        String data = encr.conRevString(Enc_Utils.enValues(items));
        params.put("data", data);
        String requestEncrypted = (new JSONObject(params)).toString();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.resetPassword(apiInterface,body);
    }


}
