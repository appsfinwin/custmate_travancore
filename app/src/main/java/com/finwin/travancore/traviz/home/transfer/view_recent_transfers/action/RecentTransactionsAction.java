package com.finwin.travancore.traviz.home.transfer.view_recent_transfers.action;

import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.pojo.transaction_list.TransactionListResponse;
import com.finwin.travancore.traviz.home.transfer.view_recent_transfers.pojo.transaction_status.TransactionStatusResponse;

public class RecentTransactionsAction {
    public static final int DEFAULT=-1;
    public static final int API_ERROR=1;
    public static final int TRANSACTIONLIST_EMPTY=2;
    public static final int TRANSACTIONLIST_SUCCESS=3;
    public static final int TRANSACTION_STATUS_SUCCESS=4;
    public static final int BACK_PRESSED=5;
    public int action;
    public String error;
    public TransactionListResponse transactionListResponse;
    public TransactionStatusResponse transactionStatusResponse;

    public RecentTransactionsAction(int action, TransactionListResponse transactionListResponse) {
        this.action = action;
        this.transactionListResponse = transactionListResponse;
    }

    public RecentTransactionsAction(int action, TransactionStatusResponse transactionStatusResponse) {
        this.action = action;
        this.transactionStatusResponse = transactionStatusResponse;
    }

    public RecentTransactionsAction(int action) {
        this.action = action;
    }

    public RecentTransactionsAction(int action, String error) {
        this.action = action;
        this.error = error;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TransactionListResponse getTransactionListResponse() {
        return transactionListResponse;
    }

    public void setTransactionListResponse(TransactionListResponse transactionListResponse) {
        this.transactionListResponse = transactionListResponse;
    }

    public TransactionStatusResponse getTransactionStatusResponse() {
        return transactionStatusResponse;
    }

    public void setTransactionStatusResponse(TransactionStatusResponse transactionStatusResponse) {
        this.transactionStatusResponse = transactionStatusResponse;
    }
}
