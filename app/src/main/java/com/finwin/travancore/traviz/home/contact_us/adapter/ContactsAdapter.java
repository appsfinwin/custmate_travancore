package com.finwin.travancore.traviz.home.contact_us.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.finwin.travancore.traviz.R;

import com.finwin.travancore.traviz.databinding.LayoutContactRowBinding;
import com.finwin.travancore.traviz.home.contact_us.action.ContactsAction;
import com.finwin.travancore.traviz.home.contact_us.pojo.ContactsData;
import com.finwin.travancore.traviz.home.transfer.view_beneficiary_list.adapter.BeneficiaryRowAction;


import java.util.Collections;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    List<ContactsData> contactsDataList;
    MutableLiveData<ContactsAction> mAction;

    public ContactsAdapter() {
        contactsDataList= Collections.emptyList();
        mAction=new MutableLiveData<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutContactRowBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_contact_row,parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setBindingData(contactsDataList.get(position), mAction);
    }

    public void setContactsDataList(List<ContactsData> contactsData)
    {
        this.contactsDataList= contactsData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutContactRowBinding binding;
        public ViewHolder(@NonNull LayoutContactRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void setBindingData(ContactsData contactsData, MutableLiveData<ContactsAction> mAction) {
            if (binding.getViewmodel()==null)
            {
                binding.setViewmodel(new ContacsRowViewModel(mAction,contactsData));
            }else {
                binding.getViewmodel().setData(contactsData);
            }
        }
    }
}
