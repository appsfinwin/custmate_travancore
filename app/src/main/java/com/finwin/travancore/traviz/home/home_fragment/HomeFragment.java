package com.finwin.travancore.traviz.home.home_fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.finwin.travancore.traviz.AdapterAndModel.Adapters.GridAdapter;
import com.finwin.travancore.traviz.AdapterAndModel.Models.GridModel;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.SupportingClass.Enc_Utils;
import com.finwin.travancore.traviz.SupportingClass.Enc_crypter;

import com.finwin.travancore.traviz.databinding.FrgHomeBinding;
import com.finwin.travancore.traviz.home.FundTransferMain;

import com.finwin.travancore.traviz.home.balance_enquiry.BalanceEnquiry;
import com.finwin.travancore.traviz.home.home_fragment.action.HomeAction;
import com.finwin.travancore.traviz.home.home_fragment.model.BannerData;
import com.finwin.travancore.traviz.home.mini_statement.MiniStatementFragment;
import com.finwin.travancore.traviz.utils.HeightWrappingViewPager;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class HomeFragment extends Fragment {

    final Enc_crypter encr = new Enc_crypter();
    private ArrayList<GridModel> homeListModelClassArrayList1;
    private RecyclerView recyclerView;
    private GridAdapter bAdapter;
    LinearLayout Btn_blnceEnqry, Btn_miniStmnt, Btn_fndTrnsfr;


    String fieldName[] = {"Mobile", "DTH", "Landline", "Data Card", "Broadband","Contact Us"};
    Integer fieldImage[] = {R.drawable.ic_mobile, R.drawable.ic_dish, R.drawable.ic_landline,
            R.drawable.ic_datacard, R.drawable.ic_broadband,R.drawable.ic_telephone};

    String[] master_name, master_value;

    static ArrayList<HashMap<String, String>> newsDataList;
    BannerAdapter bannerAdapter;
    HeightWrappingViewPager mPager;
    DotsIndicator indicator;
    ArrayList<BannerData> bannerDataList=new ArrayList<>();
    int page;
    private Handler handler;
    private int delay = 3000; //milliseconds
    Runnable runnable;
    HomeViewmodel viewmodel;
    FrgHomeBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.frg_home,container,false);
        viewmodel=new ViewModelProvider(getActivity()).get(HomeViewmodel.class);
        binding.setViewmodel(viewmodel);

        newsDataList = new ArrayList<HashMap<String, String>>();
       // handleSSLHandshake();
        handler = new Handler();
        Btn_blnceEnqry = binding.btnBlnceEnqry;
        Btn_miniStmnt = binding.btnMiniStmnt;
        Btn_fndTrnsfr = binding.btnFndTrnsfr;
        mPager = (HeightWrappingViewPager) binding.viewpager;
        indicator = (DotsIndicator) binding.indicator;

        recyclerView =binding.recyclerViewHome;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        homeListModelClassArrayList1 = new ArrayList<>();

        for (int i = 0; i < fieldName.length; i++) {
            GridModel beanClassForRecyclerView_contacts = new GridModel(fieldName[i], fieldImage[i]);
            homeListModelClassArrayList1.add(beanClassForRecyclerView_contacts);
        }
        bAdapter = new GridAdapter(getContext(), homeListModelClassArrayList1);
        recyclerView.setAdapter(bAdapter);

        try {
            if (!isNetworkOnline()) {
                askPermission();
            } else {
                getMaster();
            }
        } catch (Exception ignored) {
            Log.e("Exception news =", String.valueOf(ignored));
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewmodel.getmAction().observe(getViewLifecycleOwner(), new Observer<HomeAction>() {
            @Override
            public void onChanged(HomeAction homeAction) {
                switch (homeAction.getAction())
                {
                    case HomeAction.GET_MASTERS_SUCCESS:

                        int length = homeAction.getMastersResponse.getMasters().getData().size();
                        master_name = new String[length];
                        master_value = new String[length];
                        for (int i = 0; i < length; i++) {

                            master_name[i] = homeAction.getMastersResponse.getMasters().getData().get(i).getNAME();
                            master_value[i] = homeAction.getMastersResponse.getMasters().getData().get(i).getVALUE();
//                                mastersArray[i] = master_name[0] + "," + master_value[0];
                        }
                        ConstantClass.masterTypArray = master_name.clone();
                        ConstantClass.masterTypArrayID = master_value.clone();

                        break;
                }
            }
        });

        Btn_blnceEnqry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalanceEnquiry nextFrag = new BalanceEnquiry();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "BalanceEnquiryFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        Btn_miniStmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiniStatementFragment nextFrag = new MiniStatementFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "MiniStatementFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        Btn_fndTrnsfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FundTransferAcc nextFrag = new FundTransferAcc();
                FundTransferMain nextFrag = new FundTransferMain();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, nextFrag, "FundTransferFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        bannerDataList.clear();

        bannerDataList.add(new BannerData(R.drawable.ad_one));
        bannerDataList.add(new BannerData(R.drawable.ad_two));
        bannerDataList.add(new BannerData(R.drawable.ad_three));
        bannerDataList.add(new BannerData(R.drawable.ad_four));




        bannerAdapter =new BannerAdapter(getContext(),bannerDataList);
        mPager.setAdapter(bannerAdapter);
        mPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        indicator.setViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        runnable = new Runnable() {
            public void run() {
                if (bannerAdapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                mPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };

    }

    public void getMaster()
    {
        Map<String, String> params = new HashMap<>();
        Map<String, String> items = new HashMap<>();
        items.put("CAT_ID", "RCHG_TYPE");

        params.put("data", encr.conRevString(Enc_Utils.enValues(items)));
        viewmodel.getMasters(params);

    }





    private void askPermission() {
//        ShowNothingMsg("Please check your INTERNET Connection!!");
        Toast.makeText(getContext(), "Please check your INTERNET Connection!!", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
