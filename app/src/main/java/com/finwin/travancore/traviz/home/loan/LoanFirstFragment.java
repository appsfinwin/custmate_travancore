package com.finwin.travancore.traviz.home.loan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;
import com.finwin.travancore.traviz.SupportingClass.ErrorLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.finwin.travancore.traviz.SupportingClass.Enc_Utils.decValues;

public class LoanFirstFragment extends Fragment {

    SweetAlertDialog dialog;
    ArrayAdapter<String> adapter;
    TextView TVname, TVmobile, TVac_no;
    Spinner sp_acc;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_loan_one, container, false);


        sharedPreferences= getActivity().getSharedPreferences("com.finwin.travancore.traviz", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);

        TVname = rootView.findViewById(R.id.tv_lon_name);
        TVmobile = rootView.findViewById(R.id.tv_lon_mob);
        TVac_no = rootView.findViewById(R.id.tv_lon_accno);
        sp_acc = rootView.findViewById(R.id.Spnr_Acc_lon);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TVname.setText(sharedPreferences.getString(ConstantClass.NAME,""));
        TVmobile.setText(sharedPreferences.getString(ConstantClass.PHONE,""));
        TVac_no.setText(sharedPreferences.getString(ConstantClass.ACCOUNT_NUMBER,""));

//        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_list_item, const_acc_nos_array);
//        sp_acc.setAdapter(adapter);
//        sp_acc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               String accNumber = const_acc_nos_array[position];
//                //getAccountHolder(accNumber);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        BtnProceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mViewPager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous
////                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//                StrAmount = EdtAmount.getText().toString();
//                transfer();
//            }
//        });

    }

    public static LoanFirstFragment newInstance(String text) {
        LoanFirstFragment f = new LoanFirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

//    private void getAccountHolder(final String accountNumber) {
//        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        dialog.setTitleText("Getting Account info..");
//        dialog.setCancelable(false);
//        dialog.show();
//       // StringRequest postRequest = new StringRequest(Request.Method.POST, api_getAccountHolder,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("onResponse ==" + getActivity(), response);
//                        try {
//                            JSONObject jsonFrstRespns = new JSONObject(response);
//                            if (jsonFrstRespns.has("data")) {
//                                demessage = decValues(encr.revDecString(jsonFrstRespns.getString("data")));
//                                Log.e("demessage", demessage);
//                            }
//                            JSONObject jsonResponse = new JSONObject(demessage).getJSONObject("account");
//                            if (jsonResponse.has("data")) {
//                                JSONObject data = jsonResponse.getJSONObject("data");
//                                Name = data.getString("NAME");
//                                Mobile = data.getString("MOBILE");
//                                respndMsg = "ok";
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            respndMsg = "failed";
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            respndMsg = "accountHolderError";
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        }
//
//                        try {
//                            if (respndMsg.equals("ok")) {
//                                TVac_no.setText(ConstantClass.const_accountNumber);
//                                TVname.setText(Name);
//                                TVmobile.setText(Mobile);
//                                dialog.dismiss();
//                            } else if (respndMsg.equals("accountHolderError")) {
//                                dialog.dismiss();
//                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Oops!")
//                                        .setContentText("Account Number Freezed!")
//                                        .show();
//                            } else if (respndMsg.equals("failed")) {
//                                dialog.dismiss();
//                                new SweetAlertDialog(getActivity(), SweetAlertDialog.BUTTON_NEGATIVE)
//                                        .setTitleText("ERROR")
//                                        .setContentText("Sorry, something went Wrong.")
//                                        .show();
//                            }
//                        } catch (Exception e) {
//                            dialog.dismiss();
//                            Log.d("ERROR", "error => " + e.toString());
//                            e.printStackTrace();
//                            ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                            }.getClass().getEnclosingMethod().getName(), e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR", "error => " + error.toString());
//                        dialog.dismiss();
//                        respndMsg = "failed";
//                        ErrorLog.submitError(getActivity(), this.getClass().getSimpleName() + ":" + new Object() {
//                        }.getClass().getEnclosingMethod().getName(), error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                // the POST parameters:
//                Map<String, String> params = new HashMap<>();
//                Map<String, String> items = new HashMap<>();
//                items.put("account_no", accountNumber);
//
//                params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
//                Log.e("Getting Account info", encr.conRevString(Enc_Utils.enValues(items)));
//                return params;
//            }
//
//        };
//        requestQueue.add(postRequest);
//    }

}
