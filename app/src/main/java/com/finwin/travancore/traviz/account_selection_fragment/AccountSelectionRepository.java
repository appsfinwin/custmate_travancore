package com.finwin.travancore.traviz.account_selection_fragment;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.account_selection_fragment.action.AccountSelectionAction;
import com.finwin.travancore.traviz.home.transfer.fund_transfer_account.pojo.get_account_holder.GetAccountHolderResponse;
import com.finwin.travancore.traviz.pojo.Response;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class AccountSelectionRepository {
    public static AccountSelectionRepository instance;
    public static AccountSelectionRepository getInstance()
    {
        if (instance==null)
        {
            instance=new AccountSelectionRepository();
        }
        return instance;
    }

    MutableLiveData<AccountSelectionAction> mAction;
    CompositeDisposable disposable;
    Enc_crypter encr = new Enc_crypter();

    public MutableLiveData<AccountSelectionAction> getmAction() {
        return mAction;
    }

    public void setmAction(MutableLiveData<AccountSelectionAction> mAction) {
        this.mAction = mAction;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public void getAccountHolder(ApiInterface apiInterface, RequestBody body) {
        Single<Response> call = apiInterface.getAccountHolder(body);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response response) {

                        try {
                            String data = Enc_Utils.decValues(encr.revDecString(response.getData()));
                            data = Enc_Utils.decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            GetAccountHolderResponse getAccountHolderResponse = gson.fromJson(data, GetAccountHolderResponse.class);

                            if (getAccountHolderResponse.getAccount().getData() != null) {
                                mAction.setValue(new AccountSelectionAction(AccountSelectionAction.GET_ACCOUNT_HOLDER_SUCCESS, getAccountHolderResponse));
                            } else {
                                String error = getAccountHolderResponse.getAccount().getError();
                                mAction.setValue(new AccountSelectionAction(AccountSelectionAction.GET_ACCOUNT_HOLDER_ERROR, error));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException) {
                            mAction.setValue(new AccountSelectionAction(AccountSelectionAction.API_ERROR, "Timeout! Please try again later"));
                        } else if (e instanceof UnknownHostException) {
                            mAction.setValue(new AccountSelectionAction(AccountSelectionAction.API_ERROR, "No Internet"));
                        } else {
                            mAction.setValue(new AccountSelectionAction(AccountSelectionAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }
}
