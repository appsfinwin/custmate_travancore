package com.finwin.travancore.traviz.sign_up.sign_up;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.sign_up.sign_up.action.SignupAction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SignUpViewmodel extends AndroidViewModel {
    public SignUpViewmodel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = SignupRepository.getInstance();

        disposable = new CompositeDisposable();
        mAction = new MutableLiveData<>();

        repository.setDisposable(disposable);
        repository.setmAction(mAction);

        sharedPreferences = application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public ObservableField<String> obAccountNumber = new ObservableField<>("");
    public ObservableField<String> obName = new ObservableField<>("");
    public ObservableField<String> obMobile = new ObservableField<>("");
    public ObservableField<String> obPassword = new ObservableField<>("");
    public ObservableField<String> obConfirmPassword = new ObservableField<>("");
    Application application;
    SignupRepository repository;
    ApiInterface apiInterface;
    Enc_crypter encr = new Enc_crypter();

    CompositeDisposable disposable;
    MutableLiveData<SignupAction> mAction;


    public MutableLiveData<SignupAction> getmAction() {
        mAction = repository.getmAction();
        return mAction;
    }

    public void clickSignUp(View view) {
        if ((!obAccountNumber.get().equals("")) &&
                (!obName.get().equals("")) &&
                (!obMobile.get().equals("")) &&
                (!obPassword.get().equals("")) &&
                (!obConfirmPassword.get().equals("")) &&
                (obPassword.get().equals(obConfirmPassword.get()))
        ) {

            editor.putString(ConstantClass.ACCOUNT_NUMBER, obAccountNumber.get());
            editor.putString(ConstantClass.PHONE, obMobile.get());
            editor.putString(ConstantClass.NAME, obName.get());
            editor.putString(ConstantClass.PASSWORD, obPassword.get());
            editor.apply();
            signUp();

        } else {

        }
    }

    private void signUp() {


        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("account_no", obAccountNumber.get());

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        String request = (new JSONObject(items)).toString();
        String data = encr.conRevString(Enc_Utils.enValues(items));
        params.put("data", data);

        Map<String, Object> jsonParams = new HashMap<>();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        if (sharedPreferences.getString("login_mode","").equals("test")){
            apiInterface = RetrofitClient.RetrofitTest().create(ApiInterface.class);
        }else {
            apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        }
        //apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.getAccountHolder(apiInterface, body);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        (repository.getDisposable()).dispose();
        mAction.setValue(new SignupAction(SignupAction.DEFAULT));
    }

    public void getApiKey() {
        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.getApiKey(apiInterface);
    }

    public void clickSignIn(View view) {
        mAction.setValue(new SignupAction(SignupAction.CLICK_SIGN_IN));
    }
}

