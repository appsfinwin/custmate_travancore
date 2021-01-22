package com.finwin.travancore.traviz.sign_up.sign_up.old_classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivitySignUpDetails extends Fragment {


    SweetAlertDialog dialog;
    Button btnSignup;
    TextView tvSignin;

    String respndMsg, respMsgApi, DecKey, DecKeyGrdl, message;
    final Enc_crypter encr = new Enc_crypter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_signup_detail, container, false);

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);

        tvSignin = rootView.findViewById(R.id.tv_signin);
        btnSignup = rootView.findViewById(R.id.btn_signup);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SignUpActivity.viewPager.setCurrentItem(1, true);
////                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//                registerAPI();
            }
        });


//        BtnProceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mViewPager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous
////                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//
//            }
//        });

    }

    public static ActivitySignUpDetails newInstance(String text) {
        ActivitySignUpDetails f = new ActivitySignUpDetails();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }



}
