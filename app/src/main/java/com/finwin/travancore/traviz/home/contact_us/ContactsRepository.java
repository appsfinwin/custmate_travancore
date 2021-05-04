package com.finwin.travancore.traviz.home.contact_us;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.home.contact_us.action.ContactsAction;
import com.finwin.travancore.traviz.home.contact_us.pojo.ContactResponse;
import com.finwin.travancore.traviz.home.transfer.view_beneficiary_list.action.BeneficiaryListAction;
import com.finwin.travancore.traviz.home.transfer.view_beneficiary_list.pojo.BeneficiaryListResponse;
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

public class ContactsRepository {
    public static ContactsRepository instance;
    MutableLiveData<ContactsAction> mAction;
    CompositeDisposable disposable;
    Enc_crypter encr = new Enc_crypter();
    public static ContactsRepository getInstance()
    {
        if (instance==null)
        {
            instance=new ContactsRepository();
        }
        return instance;
    }

    public MutableLiveData<ContactsAction> getmAction() {
        return mAction;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setmAction(MutableLiveData<ContactsAction> mAction) {
        this.mAction = mAction;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public void getContacts(ApiInterface apiInterface, RequestBody body) {
        Single<ContactResponse> call = apiInterface.getContacts(body);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ContactResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(ContactResponse response) {
                    //public void onSuccess(Response response) {

                        try {
//                            String data=decValues(encr.revDecString(response.getData()));
//                            data=decValues(encr.revDecString(response.getData()));
//                            Gson gson = new GsonBuilder().create();
//                            ContactResponse contactResponse = gson.fromJson(data, ContactResponse.class);

                            if (response.getData().getTable().size()>0)
                            {
                                mAction.setValue(new ContactsAction(ContactsAction.GET_CONTAC_SUCCESS,response));
                            }
                            else
                            {
                                //String error=beneficiaryListResponse.getError();
                                mAction.setValue(new ContactsAction(ContactsAction.API_ERROR,"Please try again"));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new ContactsAction(ContactsAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new ContactsAction(ContactsAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new ContactsAction(ContactsAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }
}
