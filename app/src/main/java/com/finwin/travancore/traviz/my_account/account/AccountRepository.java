package com.finwin.travancore.traviz.my_account.account;

public class AccountRepository {
    public static AccountRepository instance;
    public static AccountRepository getInstance()
    {
        if (instance==null)
        {
            instance=new AccountRepository();
        }
        return instance;
    }
}
