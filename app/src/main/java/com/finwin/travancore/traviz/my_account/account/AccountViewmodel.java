package com.finwin.travancore.traviz.my_account.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;

import com.finwin.travancore.traviz.BR;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.databinding.FrgMyAccountBinding;
import com.finwin.travancore.traviz.login.pojo.AccNo;

public class AccountViewmodel extends AndroidViewModel implements Observable {
    public AccountViewmodel(@NonNull Application application) {
        super(application);
        setAccountNumbers();
        sharedPreferences= application.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    public ObservableArrayList<String> listAccountNumbers=new ObservableArrayList<>();
    public ObservableField<String> acountNumberSelected=new ObservableField<>("");
    public ObservableArrayList<AccNo> listAccountData=new ObservableArrayList<>();
    private int selectedAccountNumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Bindable
    public int getSelectedAccountNumber() {
        return selectedAccountNumber;
    }
    private void setAccountNumbers() {
        listAccountNumbers.clear();
        for (AccNo accountNumber: ConstantClass.accountList)
        {
            listAccountNumbers.add(accountNumber.getAccNo() +" ("+ accountNumber.getSchemeName()+" )");
            listAccountData.add(accountNumber);
        }
    }
    public void setSelectedAccountNumber(int selectedAccountNumber) {
        this.selectedAccountNumber = selectedAccountNumber;
        registry.notifyChange(this, BR.selectedAccountNumber);
    }
    public void onSelectedAccountNumber(AdapterView<?> parent, View view, int position, long id)
    {
        if (position!=0) {
            editor.putString(ConstantClass.ACCOUNT_NUMBER,ConstantClass.accountList.get(position).getAccNo());
            editor.putString(ConstantClass.SCHEME,ConstantClass.accountList.get(position).getSchemeName());
            editor.apply();
            binding.tvAccNo.setText(sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));

        }
    }
    FrgMyAccountBinding binding;
    public void setBinding(FrgMyAccountBinding binding) {
        this.binding= binding;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}
