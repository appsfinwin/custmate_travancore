package com.finwin.travancore.traviz.home.transfer.neftImps.neft;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.finwin.travancore.traviz.BR;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.databinding.FrgFundTransOneBinding;
import com.finwin.travancore.traviz.home.transfer.neftImps.pojo.neft_transfer.model.ModelTransferType;
import com.finwin.travancore.traviz.home.transfer.view_beneficiary_list.pojo.BeneficiaryData;
import com.finwin.travancore.traviz.retrofit.ApiInterface;
import com.finwin.travancore.traviz.retrofit.RetrofitClient;
import com.finwin.travancore.traviz.utils.Services;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NeftViewmodel extends AndroidViewModel implements Observable {
    public NeftViewmodel(@NonNull Application application) {
        super(application);
        this.application = application;
        mAction = new MutableLiveData<>();
        repository = NeftRepository.getInstance();
        disposable = new CompositeDisposable();

        repository.setCompositeDisposable(disposable);
        repository.setmAction(mAction);
        initSpinnerData();
        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

    }
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void initSpinnerData() {
        beneficiaryList.clear();
        beneficiaryListData.clear();
        transferTypeList.clear();
        transferTypeListData.clear();

        beneficiaryList.add("--Select Beneficiary--");
        beneficiaryListData.add(new BeneficiaryData("-1", "--Select Beneficiary--", "", ""));

        transferTypeListData.add(new ModelTransferType("--Select Transfer Type--", "-1"));
        transferTypeListData.add(new ModelTransferType("RTGS", "RT"));
        transferTypeListData.add(new ModelTransferType("NEFT", "NE"));
        transferTypeListData.add(new ModelTransferType("IMPS", "PA"));
        transferTypeListData.add(new ModelTransferType("Fund Transfer(AXIS bank only)", "FT"));

        transferTypeList.add("--Select Transfer Type--");
        transferTypeList.add("RTGS");
        transferTypeList.add("NEFT");
        transferTypeList.add("IMPS");
        transferTypeList.add("Fund Transfer(AXIS bank only)");

    }


    Enc_crypter encr = new Enc_crypter();
    Application application;
    ApiInterface apiInterface;
    MutableLiveData<NeftAction> mAction;
    NeftRepository repository;
    CompositeDisposable disposable;
    int selectedBeneficiary, selectedTransferType;
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    public ObservableArrayList<String> beneficiaryList = new ObservableArrayList<>();
    public ObservableArrayList<BeneficiaryData> beneficiaryListData = new ObservableArrayList<>();
    public ObservableArrayList<String> transferTypeList = new ObservableArrayList<>();
    public ObservableArrayList<ModelTransferType> transferTypeListData = new ObservableArrayList<>();

    public ObservableField<String> transferType_id = new ObservableField<>("-1");
    public ObservableField<String> beneficiary_account = new ObservableField<>("");
    public ObservableField<String> beneficiary_id = new ObservableField<>("-1");
    public ObservableField<String> transferAmount = new ObservableField<>("");
    public ObservableField<String> accountNumber = new ObservableField<>("");
    public ObservableField<String> ifsc = new ObservableField<>("");
    public ObservableField<Integer> accountDetailsVisibility = new ObservableField<>(View.GONE);


    public MutableLiveData<NeftAction> getmAction() {
        mAction = repository.getmAction();
        return mAction;
    }

    @Bindable
    public int getSelectedBeneficiary() {
        return selectedBeneficiary;
    }

    public void setSelectedBeneficiary(int selectedBeneficiary) {
        this.selectedBeneficiary = selectedBeneficiary;
        registry.notifyChange(this, BR.selectedBeneficiary);
    }

    @Bindable
    public int getSelectedTransferType() {
        return selectedTransferType;
    }

    public void setSelectedTransferType(int selectedTransferType) {
        this.selectedTransferType = selectedTransferType;
        registry.notifyChange(this, BR.selectedTransferType);
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

    public void validateMpin(Map<String, String> params) {
        loading.setTitleText("Please wait");
        loading.setContentText("validating..");

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.validateMpin(apiInterface, body);
    }

    public void generateOTP() {
        loading.setTitleText("Transferring..");
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("particular", "mob");
        items.put("account_no", sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));
        items.put("amount", transferAmount.get());
        items.put("agent_id", sharedPreferences.getString(ConstantClass.CUST_ID,""));
        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));

        Log.e("OTPGenerate: ", String.valueOf(items));
        Log.e("OTPGenerate data", String.valueOf(params));

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.generateOtp(apiInterface, body);
    }

    public void onSelectedBeneficiary(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {
            beneficiary_account.set(beneficiaryListData.get(position).getReceiverAccountno());
            beneficiary_id.set(beneficiaryListData.get(position).getReceiverid());

            accountDetailsVisibility.set(View.VISIBLE);
            accountNumber.set(beneficiaryListData.get(position).getReceiverAccountno());
            ifsc.set(beneficiaryListData.get(position).getReceiverIfsccode());
            //binding.edtAmount.setEnabled(true);
        } else {
            accountDetailsVisibility.set(View.GONE);
        }
    }

    public void onSelectedTransferType(AdapterView<?> parent, View view, int position, long id) {
        transferType_id.set(transferTypeListData.get(position).getTransferId());
    }

    public void getBeneficiary() {
        loading.setTitleText("Loading");
        loading.setContentText("Fetching Beneficiary Details..").show();
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("customer_id", sharedPreferences.getString(ConstantClass.CUST_ID,""));
        items.put("ben_name", "");
        items.put("ben_mobile", "");
        items.put("ben_account_no", "");
        items.put("ben_ifscode", "");
        items.put("ben_type", "ob");

        Log.e("getBeneficiary: ", String.valueOf(items));
        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        Log.e("getBeneficiary: data", encr.conRevString(Enc_Utils.enValues(items)));

        apiInterface = RetrofitClient.RetrofitClient().create(ApiInterface.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        repository.getBeneficiary(apiInterface, body);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        (repository.getCompositeDisposable()).dispose();
        mAction.setValue(new NeftAction(NeftAction.DEFAULT));
        clearData();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);

    }

    public void setBeneficiaryList(List<BeneficiaryData> data) {

        beneficiaryList.clear();
        beneficiaryListData.clear();
        beneficiaryList.add("--Select Beneficiary--");
        beneficiaryListData.add(new BeneficiaryData("-1", "--Select Beneficiary--", "", ""));

        for (BeneficiaryData beneficiaryData : data) {
            beneficiaryList.add(beneficiaryData.getReceiverName());
            beneficiaryListData.add(beneficiaryData);
        }

    }




    public void clickProceed(View view) {
        {
            if (beneficiary_id.get().equals("-1")) {
                Snackbar.make(view, "Please select beneficiary", Snackbar.LENGTH_SHORT).show();
            } else if (transferType_id.get().equals("-1")) {
                Snackbar.make(view, "Please select transfer type", Snackbar.LENGTH_SHORT).show();
            } else if (transferAmount.get().equals("")) {
                Snackbar.make(view, "Transfer amount cannot be empty!", Snackbar.LENGTH_SHORT).show();
            } else if (Double.parseDouble(transferAmount.get())>Double.parseDouble(sharedPreferences.getString(ConstantClass.CURRENT_BALANCE,""))) {
                Snackbar.make(view, "Insufficient balance!", Snackbar.LENGTH_SHORT).show();
            } else if ((!sharedPreferences.getString(ConstantClass.SCHEME,"").equals("SB"))) {
                Snackbar.make(view, "Please select a savings bank account for money transfer", Snackbar.LENGTH_SHORT).show();
            }else {
                mAction.setValue(new NeftAction(NeftAction.CLICK_PROCEED));
            }

        }
    }

    public void clearData() {

        beneficiary_id.set("-1");
        transferType_id.set("-1");
        transferAmount.set("");
        beneficiary_account.set("");
        setSelectedTransferType(0);
        setSelectedBeneficiary(0);
        accountDetailsVisibility.set(View.GONE);
    }
}
