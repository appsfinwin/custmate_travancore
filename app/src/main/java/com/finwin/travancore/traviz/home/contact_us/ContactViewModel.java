package com.finwin.travancore.traviz.home.contact_us;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.contact_us.action.ContactsAction;
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

public class ContactViewModel extends AndroidViewModel {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiInterface apiInterface;
    ContactsRepository repository;
    CompositeDisposable disposable;
    MutableLiveData<ContactsAction> mAction;
    Enc_crypter encr = new Enc_crypter();

    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository= ContactsRepository.getInstance();
        mAction= new MutableLiveData<>();
        disposable= new CompositeDisposable();
        repository.setDisposable(disposable);
        repository.setmAction( mAction);

        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }





    public MutableLiveData<ContactsAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }

    public void setmAction(MutableLiveData<ContactsAction> mAction) {
        this.mAction = mAction;
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

    public void getContacts() {
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        //items.put("Br_Code", sharedPreferences.getString(ConstantClass.BRANCH_ID,""));
        items.put("Br_Code", "01");
        items.put("Flag", "SELECT");

        Log.e("getBeneficiary: ", String.valueOf(items));
        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        apiInterface = RetrofitClient.RetrofitContacts().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(items)).toString());
        repository.getContacts(apiInterface, body);
    }
}
