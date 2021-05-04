package com.finwin.travancore.traviz.sign_up.otp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.sign_up.otp.action.SignupOtpAction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SignupOtpViewmodel extends AndroidViewModel {
    public SignupOtpViewmodel(@NonNull Application application) {
        super(application);
        this.application=application;

        repository=SignupOtpRepository.getInstance();
        disposable=new CompositeDisposable();
        mAction=new MutableLiveData<>();

        repository.setDisposable(disposable);
        repository.setmAction(mAction);

        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

    }
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SignupOtpRepository repository;
    CompositeDisposable disposable;
    MutableLiveData<SignupOtpAction> mAction;
    ApiInterface apiInterface;
    Enc_crypter encr = new Enc_crypter();
    Application application;

    public ObservableField<String> ob_otp=new ObservableField<>("");
    public MutableLiveData<SignupOtpAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        (repository.getDisposable()).dispose();
        mAction.setValue(new SignupOtpAction(SignupOtpAction.DEFAULT));
    }

    public void generateOtp() {

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("particular", "CUSTMATE_REG");
        items.put("account_no",  sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));
        items.put("amount", "0");
        items.put("agent_id", "0");
        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));


        Map<String, Object> jsonParams = new HashMap<>();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.generateOtp(apiInterface, body);
    }

    public void clickSubmit(View view)
    {
        if (ob_otp.get().equals(""))
        {
            Toast.makeText(application, "Please enter OTP", Toast.LENGTH_SHORT).show();
        }else {
            mAction.setValue(new SignupOtpAction(SignupOtpAction.CLICK_PROCEED));
        }
    }

    public void register(String otp_id) {

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("OTP_ID", otp_id);
        items.put("OTP", ob_otp.get());
        items.put("Name", sharedPreferences.getString(ConstantClass.NAME,""));
        items.put("account_no",
                sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));
        items.put("mobile_no", sharedPreferences.getString(ConstantClass.PHONE,""));
        items.put("password", sharedPreferences.getString(ConstantClass.PASSWORD,""));

        Log.e("Reg items", String.valueOf(items));

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.userRegister(apiInterface, body);

    }

    public void clickResentOtp(View view)
    {
        generateOtp();
    }
}
