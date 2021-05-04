package com.finwin.travancore.traviz.mpin_register;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.mpin_register.action.MpinRegisterAction;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.utils.Services;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MpinRegisterViewmodel extends AndroidViewModel {
    @SuppressLint("CommitPrefEdits")
    public MpinRegisterViewmodel(@NonNull Application application) {
        super(application);
        repository=MpinRegisterRepository.getInstance();
        mAction=new MutableLiveData<>();
        disposable=new CompositeDisposable();

        repository.setDisposable(disposable);
        repository.setmAction(mAction);
        this.application=application;

        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }
    Application application;
    ApiInterface apiInterface;
    MpinRegisterRepository repository;
    MutableLiveData<MpinRegisterAction> mAction;
    CompositeDisposable disposable;
    public ObservableField<String> enterMpin= new ObservableField<>("");
    public ObservableField<String> reEnterMpin= new ObservableField<>("");
    final Enc_crypter encr = new Enc_crypter();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MutableLiveData<MpinRegisterAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }


    public void validateUser(String username, String password, Context context) {

        initLoading(context);
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("username", username);
        items.put("password", password);

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.validateUser(apiInterface, body);
    }
    public void registerMpin(Context context) {

        initLoading(context);
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("userid", sharedPreferences.getString(ConstantClass.CUST_ID,""));
        items.put("MPIN", enterMpin.get());

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.registerMpin(apiInterface, body);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        (repository.getDisposable()).dispose();
        mAction.setValue(new MpinRegisterAction(MpinRegisterAction.DEFAULT));
    }

    public void submitMpin(View view)
    {
        if (enterMpin.get().equals(""))
        {
            Toast.makeText(application, "Please Enter MPIN", Toast.LENGTH_SHORT).show();
        }else if (reEnterMpin.get().equals(""))
        {
            Toast.makeText(application, "Please Re-Enter MPIN", Toast.LENGTH_SHORT).show();
        }else if (enterMpin.get().length()<6 || reEnterMpin.get().length()<6)
        {
            Toast.makeText(application, "MPIN Should Be 6 Digits", Toast.LENGTH_SHORT).show();
        }else {

            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.CLICK_SUBMIT));
        }
    }

    SweetAlertDialog loading;

    public void initLoading(Context context) {
        loading = Services.showProgressDialog(context);
        loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loading.setTitleText("Please wait...");
        loading.setCancelable(false);
    }

    public void cancelLoading() {
        if (loading != null) {
            loading.cancel();
            loading = null;
        }
    }
}
