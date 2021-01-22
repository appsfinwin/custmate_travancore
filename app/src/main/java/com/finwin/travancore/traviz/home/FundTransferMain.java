package com.finwin.travancore.traviz.home;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.home.transfer.neftImps.neft.NeftImpsFragment;
import com.finwin.travancore.traviz.home.transfer.fund_transfer_account.FundTransferAcc;
import com.finwin.travancore.traviz.home.transfer.add_beneficiary.FundTranAddBeneficiary;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.RecentTransfersActivity;

public class FundTransferMain extends Fragment {

    ImageButton iBtn_back;
    Button Btn_trnsfr, Btn_neft, Btn_benfry,btn_recent_transaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_fund_transfer_main, container, false);
        iBtn_back = rootView.findViewById(R.id.ibtn_back);
        Btn_trnsfr = rootView.findViewById(R.id.btn_f_trnsfr);
        Btn_neft = rootView.findViewById(R.id.btn_f_neft);
        Btn_benfry = rootView.findViewById(R.id.btn_f_benfry);
        btn_recent_transaction = rootView.findViewById(R.id.btn_recent_transaction);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iBtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        Btn_trnsfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FundTransferAcc nextFrag = new FundTransferAcc();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "FundTransferFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        Btn_neft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FundTransferNEFT nextFrag = new FundTransferNEFT();
               // FundTransferNeftImps nextFrag = new FundTransferNeftImps();
                NeftImpsFragment nextFrag =  NeftImpsFragment.newInstance("FirstFragment, Instance 1");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "FundTransferFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        Btn_benfry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FundTranAddBeneficiary nextFrag = new FundTranAddBeneficiary();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "FundTransferFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        btn_recent_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), RecentTransfersActivity.class);
                getActivity().startActivity(intent);

            }
        });
    }


}