package com.androidexam.fashionshop.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Fragment.HistoryBuy.OrderedDetailFragment;
import com.androidexam.fashionshop.Fragment.Individual.AddAddressFragment;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.Fragment.Pay.PaymentMethodHelper;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.ResponseOrder;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.OrderedItemViewHolder> {

    private List<ResponseOrder> orderedItemList;
    private FragmentManager fragmentManager;
    private Context context;
    private String status;

    public OrderedItemAdapter(Context context, List<ResponseOrder> orderedItemList,FragmentManager fragmentManager, String status) {
        this.context = context;
        this.orderedItemList = orderedItemList;
        this.fragmentManager =  fragmentManager;
        this.status = status;
    }

    @NonNull
    @Override
    public OrderedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_item, parent, false);
        return new OrderedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemViewHolder holder, int position) {
        int orderedId =orderedItemList.get(position).getId();
        ResponseOrder orderedItem = orderedItemList.get(position);
        // Hiển thị thông tin OrderedItem trong ViewHolder
            holder.idTextView.setText("ID                 : " + orderedItem.getId());
          holder.dateTextView.setText("Thời gian đặt hàng : " + orderedItem.getOrderDate());
        holder.statusTextView.setText("Trạng thái đơn hàng: " + (PaymentMethodHelper.paymentMethodMap.get(orderedItem.getOrderStatus())));

        int imageResource = context.getResources().getIdentifier(
                status,
                "drawable",
                context.getPackageName()
        );
        holder.image.setImageResource(imageResource);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager != null) {
                    OrderedDetailFragment orderedDetailFragment = new OrderedDetailFragment();
                    Bundle args = new Bundle();
                    args.putInt("orderedId", orderedId);
                    orderedDetailFragment.setArguments(args);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, orderedDetailFragment);
                    transaction.addToBackStack("history");
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderedItemList.size();
    }

    public class OrderedItemViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, dateTextView, statusTextView;
        ImageView image;

        public OrderedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            idTextView = itemView.findViewById(R.id.id);
            dateTextView = itemView.findViewById(R.id.date);
            statusTextView = itemView.findViewById(R.id.status);
        }
    }

}
