package com.finwin.travancore.traviz.AdapterAndModel.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.finwin.travancore.traviz.AdapterAndModel.Models.GridModel;
import com.finwin.travancore.traviz.R;
import com.finwin.travancore.traviz.home.contact_us.ContactUsActivity;
import com.finwin.travancore.traviz.home.reacharge.RechargeFragment;
import com.finwin.travancore.traviz.SupportingClass.ConstantClass;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    Context context;
    private List<GridModel> OfferList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView fieldImage;
        TextView fieldName;
        CardView tick;

        public MyViewHolder(View view) {
            super(view);
            fieldName = view.findViewById(R.id.fieldName);
            fieldImage = view.findViewById(R.id.fieldImage);
            tick = view.findViewById(R.id.tick);
        }
    }

    public GridAdapter(Context mainActivityContacts, List<GridModel> offerList) {
        this.OfferList = offerList;
        this.context = mainActivityContacts;
    }

    @Override
    public GridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new GridAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        GridModel lists = OfferList.get(position);
        holder.fieldName.setText(lists.getFieldName());
        holder.fieldImage.setImageResource(lists.getFieldImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Log.e("onClick: ", String.valueOf(position));
                if (position == 0) {
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new RechargeFragment();
//                    bundle.putString(ConstantClass.mstrType, "MOB");
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(
//                            R.id.frame_layout,
//                            myFragment).addToBackStack(null).commit();
                } else if (position == 1) {
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new RechargeFragment();
//                    bundle.putString(ConstantClass.mstrType, "DTH");
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(
//                            R.id.frame_layout,
//                            myFragment).addToBackStack(null).commit();
                } else if (position == 2) {
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new RechargeFragment();
//                    bundle.putString(ConstantClass.mstrType, "LAND");
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(
//                            R.id.frame_layout,
//                            myFragment).addToBackStack(null).commit();
                } else if (position == 3) {
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new RechargeFragment();
//                    bundle.putString(ConstantClass.mstrType, "DATA");
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(
//                            R.id.frame_layout,
//                            myFragment).addToBackStack(null).commit();
                } else if (position == 4) {
                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new RechargeFragment();
//                    bundle.putString(ConstantClass.mstrType, "LAND_BROAD");
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(
//                            R.id.frame_layout,
//                            myFragment).addToBackStack(null).commit();
                } else if (position == 5) {

                    Intent intent = new Intent (view.getContext(), ContactUsActivity.class);
                    view.getContext().startActivity(intent);

                   // Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (position == 7) {
            holder.tick.setVisibility(View.VISIBLE);
        } else {
            holder.tick.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }
}