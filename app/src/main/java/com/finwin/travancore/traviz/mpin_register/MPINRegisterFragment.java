package com.finwin.travancore.traviz.mpin_register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.finwin.travancore.traviz.activity_main.ActivityMain;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.databinding.FragmentMpinregisterBinding;
import com.finwin.travancore.traviz.mpin_register.action.MpinRegisterAction;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MPINRegisterFragment extends Fragment {

    Context context;

    final Enc_crypter encr = new Enc_crypter();
    String demessage;
    Dialog dialogMpin;
    MpinRegisterViewmodel viewmodel;
    SweetAlertDialog  successDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentMpinregisterBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        sharedPreferences= context.getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewmodel = new ViewModelProvider(getActivity()).get(MpinRegisterViewmodel.class);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_mpinregister, container, false);
        binding.setViewModel(viewmodel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<MpinRegisterAction>() {
            @Override
            public void onChanged(MpinRegisterAction mpinRegisterAction) {
                viewmodel.cancelLoading();
                switch (mpinRegisterAction.getAction()) {
                    case MpinRegisterAction.LOGIN_SUCCESS:

                        dialogMpin.dismiss();
                        viewmodel.registerMpin(context);
                        break;

                    case MpinRegisterAction.MPIN_REGISTER_SUCCESS:

                        successDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                        successDialog.setTitleText("SUCCESS")
                                .setContentText("MPIN Registered")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        startActivity(new Intent(getActivity(), ActivityMain.class));
                                        getActivity().finish();
                                        successDialog.dismiss();

                                    }
                                })
                                .show();


//                        mpin1.setEnabled(false);
//                        mpin2.setEnabled(false);
//                        submit.setEnabled(false);
                        break;

                    case MpinRegisterAction.API_ERROR:

                    case MpinRegisterAction.MPIN_REGISTER_ERROR:


                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText(mpinRegisterAction.getError())
                                .show();
                        break;

                        case MpinRegisterAction.CLICK_SUBMIT:
                            validateUserDialog();
                            break;
                }
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }

    private void validateUserDialog() {
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
                    viewmodel.validateUser(username.getText().toString(), password.getText().toString(),context);
                }
            }
        });
        dialogMpin.show();
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
