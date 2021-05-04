package com.finwin.travancore.traviz.login;

import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.login.action.LoginAction;
import com.finwin.travancore.traviz.login.pojo.LoginResponse;
import com.finwin.travancore.traviz.pojo.Response;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.sign_up.sign_up.pojo.ApiKeyResponse;
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

public class LoginRepository {
    public static LoginRepository instance;
    public static LoginRepository getInstance()
    {
        if (instance==null)
        {
            instance=new LoginRepository();
        }
        return instance;
    }

    CompositeDisposable disposable;
    MutableLiveData<LoginAction> mAction;


    final Enc_crypter encr = new Enc_crypter();
    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public MutableLiveData<LoginAction> getmAction() {
        return mAction;
    }

    public void setmAction(MutableLiveData<LoginAction> mAction) {
        this.mAction = mAction;
    }

    public void getApiKey(ApiInterface apiInterface) {
        Single<ApiKeyResponse> call = apiInterface.getApiKey();
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiKeyResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(ApiKeyResponse response) {

                        try {
                            String data=encr.revDecString(response.getData().getKey());

                            if (!data.equals(""))
                            {
                                mAction.setValue(new LoginAction(LoginAction.API_KEY_SUCCESS,data) );
                            }else
                            {
//                                String error=apiKeyResponse.getReceipt().getError();
                                mAction.setValue(new LoginAction(LoginAction.API_ERROR,"Something error"));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }

    public void login(ApiInterface apiInterface, RequestBody body) {
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
                            //String data= Enc_Utils.decValues(encr.revDecString("103322303522612603322701603501011302103221712801621621012602403902021102622212401012701203321121712911812912411622901812501201202414103411302412103802902701511712402013202302902701102901302412902302312621511022412321911912312421102022312503701022312121712402312703102402412703712022312122222022312021312402312021502011312321902011902701511011902602812222712502013211122602622103122512602111122122012302902701102302902701102901302412321302312121902022412103111022312701511011902602412111022202421122712302013602222512222111122912222203712901102302902701102302902701511712402911902302312421902302902703902302222512222203022412602103422312602103222202012602812902812222912422012302902701102302902701102901302412321901902321102011212521522402412603502011902701511011902602412111022202421122712302013602812312013111902701102302902701102302902112103102212901502022512421701802501503801802501503012302902703902912812312211111712612203111902701102302902701102302902112103102212901312602902912602122022222021502902902412222812602801502222202012211902701511011902602203122712512012302902701102302902701102901302412321901902403102011412321701802501503801802501503801411902701511011902612603103422402412122712601603122812302012302902701102302902701102901302412321901902503102011412321701802501503801802501503801411902701511011902612603103422402412122712901102302902701102302902701511712402911902912512103111402512103122022212103102402312103712022312203502621312321502011312321902011902701511011902502211103422512602111122122012302902701102302902701102901302412911802902703902912522103602702622901102302902701102901302412911802902703902302401321211322622011222311301901102302902112103321501414013012302222422611111902703902302301603812411902911902901412203902621122902012901512901301702201801012521501"));
                            data= Enc_Utils.decValues(encr.revDecString(response.getData()));
                            Gson gson = new GsonBuilder().create();
                            LoginResponse loginResponse = gson.fromJson(data, LoginResponse.class);

                            if (loginResponse.getUser().getData()!=null)
                            {
                                mAction.setValue(new LoginAction(LoginAction.LOGIN_SUCCESS,loginResponse));
                            }
                            else
                            {
                                String error=loginResponse.getUser().getError();
                                mAction.setValue(new LoginAction(LoginAction.API_ERROR,error));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof SocketTimeoutException)
                        {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR,"Timeout! Please try again later"));
                        }else if (e instanceof UnknownHostException)
                        {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR,"No Internet"));
                        }else {
                            mAction.setValue(new LoginAction(LoginAction.API_ERROR, e.getMessage()));
                        }
                    }
                });
    }
}
