package com.androidexam.fashionshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Model.Product;
import com.androidexam.fashionshop.Model.Voucher;
import com.androidexam.fashionshop.R;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VoucherAdapter  extends RecyclerView.Adapter<VoucherAdapter.ViewHolder>{

    private List<Voucher> vouchers;
    private int selectedItem = -1;
    private int selectedVoucherId = -1;
    private Context context;
    private Voucher voucher;
    private String value,imageName;
    public VoucherAdapter(Context context, List<Voucher> vouchers,String value) {
        this.context = context;
        this.vouchers = vouchers;
        this.value = value;
    }


    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item, parent, false);
        return new VoucherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(value=="FREE_SHIP"){
             imageName = "freeship";

        }
        else{
             imageName = "vouchershop";
        }



        int imageResource = context.getResources().getIdentifier(
                imageName,
                "drawable",
                context.getPackageName()
        );
        holder.imgVoucher.setImageResource(imageResource);

        int voucherIndex = position;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if (voucherIndex < vouchers.size()) {
            Voucher voucher1 = vouchers.get(voucherIndex);
            String x=voucher1.getDiscountType();

            if("AMOUNT".equals(x)){
            holder.t1.setText("Giảm "+decimalFormat.format(voucher1.getDiscountValue())+" VNĐ");
            } else if ("PERCENTAGE".equals(x)) {
                holder.t1.setText("Giảm "+voucher1.getDiscountValue()+"%");
                holder.max.setVisibility(View.GONE);
                holder.max.setVisibility(View.VISIBLE);
                holder.max.setText("Tối đa: "+decimalFormat.format(voucher1.getMaxDiscountValue())+" VNĐ");

            }
            holder.t2.setText("Hóa đơn phải trên "+decimalFormat.format(voucher1.getMinimumPurchaseAmount())+" VNĐ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            String formattedTime = voucher1.getExpiryDate().format(formatter);
            holder.hsd.setText("Hạn cuối: "+formattedTime);

            Log.d("voucher",voucher1.getExpiryDate().toString());
        }

        holder.checkBox.setChecked(position == selectedItem);


        holder.checkBox.setOnClickListener(view -> {
            if (position == selectedItem) {
                selectedItem = -1;
                selectedVoucherId = -1;
                voucher=null;
            } else {
                selectedItem = position;
                selectedVoucherId = vouchers.get(position).getId();
                voucher=vouchers.get(position);
            }
            notifyDataSetChanged();
        });


    }
    public int getSelectedVoucherId() {
        return selectedVoucherId;
    }
    public Voucher getSelectedVoucher(){
        return voucher;
    }
    public String getCode(){return voucher.getCode() ;}
    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private ImageView imgVoucher;
        private TextView t1,t2,hsd,max;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.contentFreeShip);
            t2 = itemView.findViewById(R.id.dk);
            hsd = itemView.findViewById(R.id.hsd);
            max = itemView.findViewById(R.id.max);
            imgVoucher = itemView.findViewById(R.id.im);
            checkBox = itemView.findViewById(R.id.cb_voucher);



        }
    }
}
