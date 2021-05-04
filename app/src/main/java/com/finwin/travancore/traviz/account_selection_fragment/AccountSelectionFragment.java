package com.finwin.travancore.traviz.account_selection_fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.activity_main.ActivityMain;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.databinding.FragmentAccountSelectionBinding;
import com.finwin.travancore.traviz.mpin_register.MPINRegisterFragment;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.account_selection_fragment.action.AccountSelectionAction;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;

import java.util.Arrays;
import java.util.function.Predicate;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccountSelectionFragment extends Fragment {
    TextView name, ac_no, mobile;
    SweetAlertDialog dialog;
    CardView cv_ac_details;
    Button ok;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    final Enc_crypter encr = new Enc_crypter();

    AccountSelectionViewmodel viewmodel;
    FragmentAccountSelectionBinding binding;


    public AccountSelectionFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AccountSelectionFragment newInstance(String title) {
        AccountSelectionFragment frag = new AccountSelectionFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedPreferences= getActivity().getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_account_selection,container,false);
        viewmodel=new ViewModelProvider(getActivity()).get(AccountSelectionViewmodel.class);
        binding.setViewmodel(viewmodel);
        viewmodel.setBinding(binding);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);

        name = binding.tvAcName;
        ac_no = binding.tvAcAccNo;
        mobile = binding.tvAcMobile;
        ok = binding.btnContinue;



        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<AccountSelectionAction>() {
            @Override
            public void onChanged(AccountSelectionAction accountSelectionAction) {
                switch (accountSelectionAction.getAction())
                {
                    case AccountSelectionAction.GET_ACCOUNT_HOLDER_SUCCESS:
                        viewmodel.cancelLoading();
                        name.setText(accountSelectionAction.getAccountHolderResponse.getAccount().getData().getName());
                        mobile.setText( accountSelectionAction.getAccountHolderResponse.getAccount().getData().getMobile());

                        String amount=getAmount(accountSelectionAction.getAccountHolderResponse.getAccount().getData().getCurrentBalance());
                        editor.putString(ConstantClass.CURRENT_BALANCE,amount);
                        editor.apply();
                        break;

                        case AccountSelectionAction.API_ERROR:

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops!")
                                    .setContentText(accountSelectionAction.getError())
                                    .show();
                            break;
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean(ConstantClass.MPIN_STATUS,false)) {

                    startActivity(new Intent(getActivity(), ActivityMain.class));
                    getActivity().finish();
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    assert fragmentManager != null;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MPINRegisterFragment fragment = new MPINRegisterFragment();
                    fragmentTransaction.replace(R.id.frame_layout_login,fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private String getAmount(String str) {

        String myString = str.replaceAll("[^0-9\\.]","");
        return myString;
    }


}
