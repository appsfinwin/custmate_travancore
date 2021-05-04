package com.finwin.travancore.traviz.my_account.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.ActivityInitial;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.databinding.FrgMyAccountBinding;
import com.finwin.travancore.traviz.my_account.update_mpin.UpdateMPINFragment;

import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentAccount extends Fragment {

    SweetAlertDialog dialog;
    final Enc_crypter encr = new Enc_crypter();
    FrgMyAccountBinding binding;
    AccountViewmodel viewmodel;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(inflater,R.layout.frg_my_account, container, false);
        viewmodel= new ViewModelProvider(this).get(AccountViewmodel.class);
        viewmodel.setBinding(binding);
        binding.setViewModel(viewmodel);


        sharedPreferences= getActivity().getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);


        binding.tvAccName.setText(sharedPreferences.getString(ConstantClass.NAME,""));
        binding.tvAccNo.setText( sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));
        binding.tvAccMob.setText(sharedPreferences.getString(ConstantClass.PHONE,""));

        viewmodel.setSelectedAccountNumber(viewmodel.listAccountNumbers.indexOf(( sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,"") +" ("+ sharedPreferences.getString(ConstantClass.SCHEME,"")+" )")));


        ConstraintLayout linear_changeMpin =binding.linearChangeMpin;
        linear_changeMpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UpdateMPINFragment containerFragment = new UpdateMPINFragment();
                fragmentTransaction.replace(R.id.frame_layout, containerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ConstraintLayout linearSignout = binding.linearSignout;
        linearSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertsignout();
            }
        });
    }


    public void alertsignout() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        // Setting Dialog Title
        alertDialog2.setTitle("Confirm SignOut");
        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to Sign out?");
        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent i = new Intent(getActivity(), ActivityInitial.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }

}
