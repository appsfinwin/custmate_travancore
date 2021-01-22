package com.finwin.travancore.traviz.home.mini_statement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.finwin.travancore.traviz.AdapterAndModel.Adapters.MiniStmntAdapter;

import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.databinding.FrgMiniStatementBinding;
import com.finwin.travancore.traviz.home.mini_statement.action.MiniStatementAction;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MiniStatementFragment extends Fragment {
    final Enc_crypter encr = new Enc_crypter();
    private MiniStmntAdapter bAdapter;

    SweetAlertDialog pDialog;
    ImageButton iBtn_back;
    private RecyclerView recyclerView;
    MiniStatementViewmodel viewmodel;
    FrgMiniStatementBinding binding;
    MiniStmntAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(inflater, R.layout.frg_mini_statement, container, false);
        viewmodel=new ViewModelProvider(getActivity()).get(MiniStatementViewmodel.class);
        binding.setViewmodel(viewmodel);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);


        recyclerView = binding.recViewStmnt;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new MiniStmntAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        viewmodel.getMiniStatement();
        viewmodel.initLoading(getActivity());



        iBtn_back = binding.ibtnBack;
        iBtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<MiniStatementAction>() {
            @Override
            public void onChanged(MiniStatementAction miniStatementAction) {
                viewmodel.cancelLoading();
                switch (miniStatementAction.getAction())
                {
                    case MiniStatementAction.MINI_STATEMENT_SUCCESS:

                        binding.tvaccBalance.setText(miniStatementAction.getMiniStatementResponse().getMiniStatement().getData().getCURRENTBALANCE());
                        binding.tvaccNo.setText(miniStatementAction.getMiniStatementResponse().getMiniStatement().getData().getACCNO());
                        adapter.setTransactonList(miniStatementAction.getMiniStatementResponse().getMiniStatement().getData().getTRANSACTONS());
                        adapter.notifyDataSetChanged();
                        break;


                        case MiniStatementAction.API_ERROR:

                            break;
                }
            }
        });
    }



}
