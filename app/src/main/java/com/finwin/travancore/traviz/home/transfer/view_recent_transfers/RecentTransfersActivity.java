package com.finwin.travancore.traviz.home.transfer.view_recent_transfers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.databinding.ActivityRecentTransfersBinding;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.action.RecentTransactionsAction;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.adapter.TransactionListAdapter;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.adapter.TransactionListRowAction;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.transaction_details.TransactionDetailsActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecentTransfersActivity extends AppCompatActivity {

    RecentTransfersViewmodel viewmodel;
    ActivityRecentTransfersBinding binding;
    TransactionListAdapter transactionListAdapter;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this, R.layout.activity_recent_transfers);
        viewmodel=new ViewModelProvider(this).get(RecentTransfersViewmodel.class);
        binding.setViewmodel(viewmodel);

        showProgress();
        viewmodel.getRecentTransactions();


        setRecyclerView(binding.rvTransactionList);

        viewmodel.getmAction().observe(this, new Observer<RecentTransactionsAction>() {
            @Override
            public void onChanged(RecentTransactionsAction recentTransactionsAction) {
                cancelProgress();

                switch (recentTransactionsAction.getAction())
                {
                    case RecentTransactionsAction.TRANSACTIONLIST_SUCCESS:

                        transactionListAdapter.setTransactionDataList(recentTransactionsAction.getTransactionListResponse().getBen().getData());
                        transactionListAdapter.notifyDataSetChanged();

                        break;

                        case RecentTransactionsAction.TRANSACTIONLIST_EMPTY:
                            new SweetAlertDialog(RecentTransfersActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR")
                                    .setContentText("No transaction found!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                            break;

                            case RecentTransactionsAction.BACK_PRESSED:
                                finish();
                                break;
                }
            }
        });

    }

    private void setRecyclerView(RecyclerView rvTransactionList) {

        transactionListAdapter=new TransactionListAdapter();
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
        rvTransactionList.setAdapter(transactionListAdapter);

        setObservable(transactionListAdapter);

    }

    private void setObservable(TransactionListAdapter transactionListAdapter) {
        transactionListAdapter.getmAction().observe(this, new Observer<TransactionListRowAction>() {
            @Override
            public void onChanged(TransactionListRowAction transactionListRowAction) {
                switch (transactionListRowAction.getAction())
                {
                    case TransactionListRowAction.ITEM_CLICK:
                    {
                        Intent intent=new Intent(RecentTransfersActivity.this, TransactionDetailsActivity.class);
                        intent.putExtra("cust_unique_number",transactionListRowAction.getTransactionData().getCustomerUniqueNo());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showProgress() {

        sweetAlertDialog = new SweetAlertDialog(RecentTransfersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText("Please wait")
                .show();
    }
    private void cancelProgress() {
        if (sweetAlertDialog!=null)
        {
            sweetAlertDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showProgress();
        viewmodel.getRecentTransactions();
    }
}