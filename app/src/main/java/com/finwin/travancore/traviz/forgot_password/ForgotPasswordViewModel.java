package com.finwin.travancore.traviz.forgot_password;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.utils.Services;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgotPasswordViewModel extends AndroidViewModel {

    CompositeDisposable compositeDisposable;
    MutableLiveData<ForgotPasswordAction> mAction;
    Enc_crypter encr = new Enc_crypter();
    ForgotPasswordRepository repository= ForgotPasswordRepository.getInstance();
    Application application;
    ApiInterface apiInterface;

    public ObservableField<String> accountNumber= new ObservableField<>("");
    public ForgotPasswordViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application=application;
        mAction= new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
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
        mAction= repository.getmAction();
        return mAction;
    }

    public void setmAction(MutableLiveData<ForgotPasswordAction> mAction) {
        this.mAction = mAction;
    }

    public void clickSentOtp(View view)
    {
        if (accountNumber.get().equals(""))
        {
            Toast.makeText(application, "Please enter account number", Toast.LENGTH_SHORT).show();
        }else {

            initLoading(view.getContext());
           sentOtp();
        }

    }

    private void sentOtp() {
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
        repository.forgotPassword(apiInterface,body);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        (repository.getCompositeDisposable()).dispose();
    }
}
