package com.finwin.travancore.traviz.home.transfer.view_recent_transfers;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.action.RecentTransactionsAction;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.pojo.transaction_list.TransactionListResponse;
import com.finwin.travancore.traviz.pojo.Response;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
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

import static com.finwin.travancore.traviz.SupportingClass.Enc_Utils.decValues;

public class RecentTransfersRepository {

    public static RecentTransfersRepository instance;
    public static RecentTransfersRepository getInstance()
    {
        if (instance==null)
        {
            instance=new RecentTransfersRepository();
        }
        return instance;
    }

    final Enc_crypter encr = new Enc_crypter();
    CompositeDisposable disposable;
    MutableLiveData<RecentTransactionsAction> mAction;

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public MutableLiveData<RecentTransactionsAction> getmAction() {
        return mAction;
    }

    public void setmAction(MutableLiveData<RecentTransactionsAction> mAction) {
        this.mAction = mAction;
    }

    public void getTransactionList(ApiInterface apiInterface, RequestBody body) {
        Single<Response> call = apiInterface.getRecentTransactionList(body);
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
                            String data=decValues(encr.revDecString(response.getData()));
                            data=decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            TransactionListResponse transactionListResponse = gson.fromJson(data, TransactionListResponse.class);

                            if (transactionListResponse.getBen().getData().size()>0)
                            {
                                mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.TRANSACTIONLIST_SUCCESS,transactionListResponse));
                            }
                            else
                            {
                                mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.TRANSACTIONLIST_EMPTY));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new RecentTransactionsAction(RecentTransactionsAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }


}
