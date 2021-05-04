package com.finwin.travancore.traviz.home.transfer.view_recent_transfers;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.action.RecentTransactionsAction;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RecentTransfersViewmodel extends AndroidViewModel {
    public RecentTransfersViewmodel(@NonNull Application application) {
        super(application);

        repository=RecentTransfersRepository.getInstance();
        disposable=new CompositeDisposable();
        mAction=new MutableLiveData<>();

        repository.setDisposable(disposable);
        repository.setmAction(mAction);
        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }
    Enc_crypter encr = new Enc_crypter();
    ApiInterface apiInterface;
    RecentTransfersRepository repository;
    CompositeDisposable disposable;
    MutableLiveData<RecentTransactionsAction> mAction;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public LiveData<RecentTransactionsAction> getmAction() {
        mAction=repository.getmAction();
        return mAction;
    }

    public void getRecentTransactions() {
        Map<String, Object> jsonParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        jsonParams.put("customer_id", sharedPreferences.getString(ConstantClass.CUST_ID,""));

        params.put("data", encr.conRevString(Enc_Utils.enValues(jsonParams)));

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        repository.getTransactionList(apiInterface,body);
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        (repository.getDisposable()).dispose();
        mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.DEFAULT));
    }

    public void backPressed(View view)
    {
        mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.BACK_PRESSED));
    }
}
