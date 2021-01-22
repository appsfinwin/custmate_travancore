package com.finwin.travancore.traviz.home.mini_statement;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.mini_statement.action.MiniStatementAction;
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

public class MiniStatementViewmodel extends AndroidViewModel {
    public MiniStatementViewmodel(@NonNull Application application) {
        super(application);

        repository=MiniStatementRepository.getInstance();
        mAction=new MutableLiveData<>();
        compositeDisposable=new CompositeDisposable();

        repository.setCompositeDisposable(compositeDisposable);
        repository.setmAction(mAction);
    }

    MiniStatementRepository repository;
    CompositeDisposable compositeDisposable;
    MutableLiveData<MiniStatementAction> mAction;
    Enc_crypter encr = new Enc_crypter();
    ApiInterface apiInterface;

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

    public MutableLiveData<MiniStatementAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }

    public void setmAction(MutableLiveData<MiniStatementAction> mAction) {
        this.mAction = mAction;
    }

    public void getMiniStatement() {

        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("account_no", ConstantClass.const_accountNumber);

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.getMiniStatement(apiInterface, body);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        (repository.getCompositeDisposable()).dispose();
        mAction.setValue(new MiniStatementAction(MiniStatementAction.DEFAULT));

    }
}
