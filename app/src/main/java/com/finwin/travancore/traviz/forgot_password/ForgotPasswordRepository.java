package com.finwin.travancore.traviz.forgot_password;

import androidx.lifecycle.MutableLiveData;


import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.forgot_password.pojo.ResetPasswordResponse;
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


public class ForgotPasswordRepository {

    CompositeDisposable compositeDisposable;
    MutableLiveData<ForgotPasswordAction> mAction;
    Enc_crypter encr = new Enc_crypter();

    public static ForgotPasswordRepository INSTANCE;

    public static ForgotPasswordRepository getInstance()
    {
        if (INSTANCE==null)
        {
            INSTANCE=new ForgotPasswordRepository();
        }
        return INSTANCE;
    }

    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public void setmAction(MutableLiveData<ForgotPasswordAction> mAction) {
        this.mAction = mAction;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public MutableLiveData<ForgotPasswordAction> getmAction() {
        return mAction;
    }

    public void forgotPassword(ApiInterface apiInterface, RequestBody body) {
        Single<Response> call = apiInterface.forgotPassword(body);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response response) {

                        try {
                            String data=decValues(encr.revDecString(response.getData()));
                            data=decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            ResetPasswordResponse resetPasswordResponse = gson.fromJson(data, ResetPasswordResponse.class);

                            if (resetPasswordResponse.getStatus().equals("1"))
                            {
                                mAction.setValue(new ForgotPasswordAction(ForgotPasswordAction.SENT_OTP_SUCCESS,resetPasswordResponse));
                            }
                            else
                            {
                                mAction.setValue(new ForgotPasswordAction(ForgotPasswordAction.API_ERROR,resetPasswordResponse.getReturnMessage()));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new ForgotPasswordAction(ForgotPasswordAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new ForgotPasswordAction(ForgotPasswordAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new ForgotPasswordAction(ForgotPasswordAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }

}
