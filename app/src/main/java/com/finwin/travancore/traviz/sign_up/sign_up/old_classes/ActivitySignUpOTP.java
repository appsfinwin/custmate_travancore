package com.finwin.travancore.traviz.sign_up.sign_up.old_classes;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivitySignUpOTP extends Fragment {

    Enc_crypter encr = new Enc_crypter();

    SweetAlertDialog sweetDialog;

    Button btn_SubmitOTP;
    EditText edt_OTP;
    TextView tv_ReSendOTP;
    String demessage, TrMsg, error,
            StrOTP_data, StrOTP_id,
            Str_OTP;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_signup_otp, container, false);
        sweetDialog = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);

        edt_OTP = rootView.findViewById(R.id.edt_signup_otp);
        tv_ReSendOTP = rootView.findViewById(R.id.tv_signup_resend_otp);
        btn_SubmitOTP = rootView.findViewById(R.id.btn_signup_sbmt);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_SubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_OTP.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    btn_SubmitOTP.setEnabled(false);
                    Str_OTP = edt_OTP.getText().toString();
//                    Transfer();
                }
            }
        });

        tv_ReSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ReSendOTP.setEnabled(false);
//                re_generateOTP();
            }
        });
    }

    public static ActivitySignUpOTP newInstance(String text) {
        ActivitySignUpOTP f = new ActivitySignUpOTP();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }


}
