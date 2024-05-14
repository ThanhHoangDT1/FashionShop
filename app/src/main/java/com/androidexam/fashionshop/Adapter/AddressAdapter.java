package com.androidexam.fashionshop.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Fragment.Individual.AddAddressFragment;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressItem> addressItemList;
    private FragmentManager fragmentManager;
    private int id;
    private String method;
    private ArrayList<CartItem> cartItemArrayList ;
    private ArrayList<Integer> listIds;
    public AddressAdapter(List<AddressItem> addressItemList, FragmentManager fragmentManager,int id,ArrayList<CartItem> cartItemArrayList,ArrayList<Integer> listIds,String method) {
        this.addressItemList = addressItemList;
        this.fragmentManager = fragmentManager;
        this.id =id;
        this.cartItemArrayList=cartItemArrayList;
        this.listIds = listIds;
        this.method = method;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int addressIndex = position;

        if (addressIndex < addressItemList.size()) {
            AddressItem addressItem = addressItemList.get(addressIndex);
            if (addressItem != null) {

                holder.name.setText(addressItem.getName());
                holder.phoneNumber.setText(addressItem.getPhoneNumber());
                holder.address.setText(addressItem.getAddress());
                holder.street.setText(addressItem.getStreet());
                if(addressItem.isDefault()){
                    holder.myswitch.setVisibility(View.VISIBLE);
                }else {
                    holder.myswitch.setVisibility(View.GONE);
                }
                //holder.myswitch.setChecked(addressItem.isDefault());

            } else {
            }
        }else {
        }
        int addressId = addressItemList.get(addressIndex).getId();

        if(id==0){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressItem item = addressItemList.get(addressIndex);

                AddAddressFragment addAddressFragment = new AddAddressFragment();
                Bundle args = new Bundle();
                args.putSerializable("addressItem", item);
                addAddressFragment.setArguments(args);

                // Start a Fragment transaction to replace the current Fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, addAddressFragment);
                transaction.addToBackStack("address");
                transaction.commit();
            }
        });} else if (id==1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayFragment payFragment = new PayFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("cartItemList", cartItemArrayList);
                    args.putIntegerArrayList("listId", listIds);
                    args.putString("method",method);
                    AddressItem item = addressItemList.get(addressIndex);
                    args.putSerializable("addressChosen", item);
                    payFragment.setArguments(args);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, payFragment);
                    transaction.commit();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return addressItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phoneNumber;
        TextView address;
        TextView street;
        TextView myswitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_item);
            phoneNumber = itemView.findViewById(R.id.phonenumber_item);
            address = itemView.findViewById(R.id.address_item);
            street = itemView.findViewById(R.id.street);
            myswitch = itemView.findViewById(R.id.defaul);
        }
    }
}
