package com.finwin.travancore.traviz.home.balance_enquiry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.databinding.FrgBalanceEnquiryBinding;
import com.finwin.travancore.traviz.home.balance_enquiry.action.BalanceAction;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class BalanceEnquiry extends Fragment {
    Enc_crypter encr = new Enc_crypter();
    SweetAlertDialog pDialog, dialogue;


    TextView TvDate, TvTime, TvAcc_no, TvName, TvMobile, TvBalance;
    ImageButton iBtn_back;
    String demessage, rspndMsg,
            jdate, jaccount_no, jname, jphone, jbalance;

    BalanceViewmodel viewmodel;
    FrgBalanceEnquiryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_balance_enquiry, container, false);
        viewmodel = new ViewModelProvider(getActivity()).get(BalanceViewmodel.class);
        binding.setViewmodel(viewmodel);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);

        viewmodel.balanceEnquiry();
        viewmodel.initLoading(getActivity());
        TvDate = binding.tvdate;
        TvTime = binding.tvtime;
        TvAcc_no = binding.tvaccNumber;
        TvName = binding.tvaccName;
        TvBalance = binding.tvbalance;



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<BalanceAction>() {
            @Override
            public void onChanged(BalanceAction balanceAction) {

                viewmodel.cancelLoading();
                switch (balanceAction.getAction()) {
                    case BalanceAction.GET_BALANCE_SUCCESS:

                        viewmodel.setBalance(balanceAction.getBalanceResponse().getBalance());
                        break;
                    case BalanceAction.API_ERROR:
                        dialogue = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                        dialogue.setTitleText("Error!!");
                        dialogue.setContentText(balanceAction.getError());
                        dialogue.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                FragmentManager fm = getFragmentManager();
                                fm.popBackStack();
                                dialogue.dismiss();
                            }
                        }).show();
                        break;

                        case BalanceAction.CLICK_BACK:
                            getFragmentManager().popBackStack();
                            break;
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        viewmodel.getmAction().setValue(new BalanceAction(BalanceAction.DEFAULT));
    }
}
