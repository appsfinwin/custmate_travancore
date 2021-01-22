package com.finwin.travancore.traviz.mpin_register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.activity_main.ActivityMain;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.mpin_register.action.MpinRegisterAction;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MPINRegisterFragment extends Fragment {

    Button submit;
    //    PasswordView mpin1, mpin2;
    AppCompatEditText mpin1, mpin2;
    Context context;

    final Enc_crypter encr = new Enc_crypter();
    String demessage;
    Dialog dialogMpin;
    MpinRegisterViewmodel viewmodel;
    SweetAlertDialog sweetAlertDialog, successDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewmodel = new ViewModelProvider(getActivity()).get(MpinRegisterViewmodel.class);
        return inflater.inflate(R.layout.fragment_mpinregister, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mpin1 = view.findViewById(R.id.et_mpin_1);
        mpin2 = view.findViewById(R.id.et_mpin_2);
        submit = view.findViewById(R.id.btn_mpin_submit);

        mpin1.setEnabled(true);
        mpin2.setEnabled(true);
        submit.setEnabled(true);
        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<MpinRegisterAction>() {
            @Override
            public void onChanged(MpinRegisterAction mpinRegisterAction) {
                switch (mpinRegisterAction.getAction()) {
                    case MpinRegisterAction.LOGIN_SUCCESS:

                        dialogMpin.dismiss();
                        registerMpin();
                        break;

                    case MpinRegisterAction.MPIN_REGISTER_SUCCESS:
                        sweetAlertDialog.dismiss();
                        successDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        successDialog.setTitleText("SUCCESS")
                                .setContentText("MPIN Registered")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {



//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        // assert fragmentManager != null;
//                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                        HomeFragment containerFragment = new HomeFragment();
//                                        fragmentTransaction.replace(R.id.frame_layout_login, containerFragment);
//                                        fragmentTransaction.addToBackStack(null);
//                                        fragmentTransaction.commit();

                                        startActivity(new Intent(getActivity(), ActivityMain.class));
                                        getActivity().finish();
                                        successDialog.dismiss();

                                    }
                                })
                                .show();


                        mpin1.setEnabled(false);
                        mpin2.setEnabled(false);
                        submit.setEnabled(false);
                        break;

                    case MpinRegisterAction.API_ERROR:

                    case MpinRegisterAction.MPIN_REGISTER_ERROR:

                        sweetAlertDialog.dismiss();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText(mpinRegisterAction.getError())
                                .show();
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpin1.getText().toString().equals(mpin2.getText().toString())) {
                    if (mpin1.getText().toString().length() == 6) {
                        dialogMpin = new Dialog(context);
                        dialogMpin.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogMpin.setCancelable(true);
                        dialogMpin.setContentView(R.layout.validation);
                        final Button validate = dialogMpin.findViewById(R.id.btn_validate);
                        final EditText username = dialogMpin.findViewById(R.id.et_username);
                        final EditText password = dialogMpin.findViewById(R.id.et_password);
                        validate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Username & Password cannot be empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    validateUser(username.getText().toString(), password.getText().toString());
                                }
                            }
                        });
                        dialogMpin.show();
                    } else {
                        Toast.makeText(getActivity(), "MPIN Should Be 6 Digits", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "MPIN do not match!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void validateUser(String username, String password) {
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("username", username);
        items.put("password", password);

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        viewmodel.validateUser(params);

    }

    public void registerMpin() {

        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Please wait")
                .show();
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("userid", ConstantClass.const_cusId);
        items.put("MPIN", mpin2.getText().toString());



        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        viewmodel.registerMpin(params);

    }

    @Override
    public void onStop() {
        super.onStop();
        viewmodel.getmAction().setValue(new MpinRegisterAction(MpinRegisterAction.DEFAULT));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewmodel!=null) {
            viewmodel.getmAction().setValue(new MpinRegisterAction(MpinRegisterAction.DEFAULT));
        }
    }
}
