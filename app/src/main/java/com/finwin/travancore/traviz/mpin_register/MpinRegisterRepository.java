package com.finwin.travancore.traviz.mpin_register;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.login.pojo.LoginResponse;
import com.finwin.travancore.traviz.mpin_register.action.MpinRegisterAction;
import com.finwin.travancore.traviz.mpin_register.pojo.MpinRegisterResponse;
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

public class MpinRegisterRepository {
    public static MpinRegisterRepository instance;
    public static MpinRegisterRepository getInstance()
    {
        if (instance==null)
        {
            instance=new MpinRegisterRepository();
        }
        return instance;
    }

    MutableLiveData<MpinRegisterAction> mAction;
    CompositeDisposable disposable;
    final Enc_crypter encr = new Enc_crypter();

    public MutableLiveData<MpinRegisterAction> getmAction() {
        return mAction;
    }

    public void setmAction(MutableLiveData<MpinRegisterAction> mAction) {
        this.mAction = mAction;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public void registerMpin(ApiInterface apiInterface, RequestBody body) {
        Single<Response> call = apiInterface.registerMpin(body);
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
                            String data= Enc_Utils.decValues(encr.revDecString(response.getData()));
                            data= Enc_Utils.decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            MpinRegisterResponse mpinRegisterResponse = gson.fromJson(data, MpinRegisterResponse.class);

                            if (mpinRegisterResponse.getStatus().equals("1"))
                            {
                                mAction.setValue(new MpinRegisterAction(MpinRegisterAction.MPIN_REGISTER_SUCCESS,mpinRegisterResponse));
                            }
                            else
                            {
                                mAction.setValue(new MpinRegisterAction(MpinRegisterAction.MPIN_REGISTER_ERROR,mpinRegisterResponse.getMsg()));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }public void validateUser(ApiInterface apiInterface, RequestBody body) {
        Single<Response> call = apiInterface.login(body);
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
                            String data= Enc_Utils.decValues(encr.revDecString(response.getData()));
                            data= Enc_Utils.decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            LoginResponse loginResponse = gson.fromJson(data, LoginResponse.class);

                            if (loginResponse.getUser().getData()!=null)
                            {
                                mAction.setValue(new MpinRegisterAction(MpinRegisterAction.LOGIN_SUCCESS,loginResponse));
                            }
                            else
                            {
                                String error=loginResponse.getUser().getError();
                                mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR,error));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new MpinRegisterAction(MpinRegisterAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }
}
