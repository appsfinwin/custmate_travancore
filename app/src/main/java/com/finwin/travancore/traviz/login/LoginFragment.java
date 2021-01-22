package com.finwin.travancore.traviz.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.BuildConfig;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.account_selection_fragment.AccountSelectionFragment;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.SupportingClass.ErrorLog;


import com.finwin.travancore.traviz.databinding.LayoutSiginInBinding;
import com.finwin.travancore.traviz.login.action.LoginAction;
import com.finwin.travancore.traviz.sign_up.sign_up.SignUpActivity;
import com.finwin.travancore.traviz.utils.VersionChecker;


import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginFragment extends Fragment {

    String  apiKeyStored = "";
    final Enc_crypter encr = new Enc_crypter();
    private ProgressBar progress;

    LoginViewmodel viewmodel;

    String latestVersion="",currentVersion="";
    LayoutSiginInBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_sigin_in, container, false);
        viewmodel=new ViewModelProvider(getActivity()).get(LoginViewmodel.class);
        binding.setViewmodel(viewmodel);


        viewmodel.setBinding(binding);
        viewmodel.initLoading(getContext());
        if (isNetworkOnline()) {
            checkVersion();
        }else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<LoginAction>() {
            @Override
            public void onChanged(LoginAction loginAction) {

               // binding.progress.setVisibility(GONE);
                switch (loginAction.getAction()) {
                    case LoginAction.API_KEY_SUCCESS:
                        apiKeyStored = encr.revDecString(encr.decrypter(BuildConfig.AP_KE));
                        if (apiKeyStored.equals(loginAction.getError())) {
                            viewmodel.login();
                        }
                        break;

                    case LoginAction.CLICK_SIGNUP:

                        Intent i = new Intent(getContext(), SignUpActivity.class);
                        startActivity(i);
                        break;

                    case LoginAction.API_ERROR:

                        viewmodel.cancelLoading();
                        new SweetAlertDialog(getContext(), SweetAlertDialog.BUTTON_NEGATIVE)
                                .setTitleText("ERROR")
                                .setContentText(loginAction.getError())
                                .show();
                        break;

                        case LoginAction.LOGIN_SUCCESS:
                            viewmodel.cancelLoading();
                            ConstantClass.const_name = loginAction.getLoginResponse().getUser().getData().getUSERNAME();
                            ConstantClass.const_phone = loginAction.getLoginResponse().getUser().getData().getMOBILENO();
                            ConstantClass.const_cusId =loginAction.getLoginResponse().getUser().getData().getUSERID();
                            ConstantClass.mpinStatus = loginAction.getLoginResponse().getUser().getData().getMPINstatus();
                            ConstantClass.listAccountNumbers=new ArrayList<>();
                            ConstantClass.listAccountNumbers.clear();
                            ConstantClass.listScheme=new ArrayList<>();
                            ConstantClass.listScheme.clear();


                            int size=loginAction.getLoginResponse().getUser().getData().getAccNo().size();
                            for (int j=0;j<size;j++)
                            {
                                ConstantClass.listAccountNumbers.add(loginAction.getLoginResponse().getUser().getData().getAccNo().get(j).getAccNo());
                                ConstantClass.listScheme.add(loginAction.getLoginResponse().getUser().getData().getAccNo().get(j).getSchname());
                            }
                            openAccountSelectionFrag();

                            break;

                }
            }
        });

    }
    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
    private void checkVersion() {

        VersionChecker versionChecker = new VersionChecker();
        try {

            latestVersion = versionChecker.execute().get();
            // Toast.makeText(getActivity().getApplicationContext(), latestVersion , Toast.LENGTH_SHORT).show();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert info != null;
        currentVersion = info.versionName;
        if (latestVersion==null)
        {
            viewmodel.cancelLoading();
            Toast.makeText(getActivity().getApplicationContext(), "Slow network Detected!", Toast.LENGTH_SHORT).show();
        }else {
            viewmodel.cancelLoading();
            if (Float.parseFloat(currentVersion) < Float.parseFloat(latestVersion)) {
                showUpdateDialog();
            }
        }
    }
    private void openAccountSelectionFrag() {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Class fragmentClass = AccountSelectionFragment.class;
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_login, fragment).commit();

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
//            ErrorLog.submitError(getContext(), this.getClass().getSimpleName() + ":" + new Object() {
//            }.getClass().getEnclosingMethod().getName(), e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
//            ErrorLog.submitError(getContext(), this.getClass().getSimpleName() + ":" + new Object() {
//            }.getClass().getEnclosingMethod().getName(), e.toString());
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (isNetworkOnline()) {
//            checkVersion();
//        }else {
//            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void showUpdateDialog() {


        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
        // Setting Dialog Title
        alertDialog2.setTitle("Update Available");
        // Setting Dialog Message
        alertDialog2.setMessage("There is a newer version of this application is available");
        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.finwin.travancore.traviz"));
                        startActivity(i);
                    }
                });
        alertDialog2.setCancelable(false);
        alertDialog2.show();

    }

}
