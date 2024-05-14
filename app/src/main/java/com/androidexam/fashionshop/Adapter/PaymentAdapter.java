package com.androidexam.fashionshop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<CartItem> paymentList;
    private Context context;

    public PaymentAdapter(Context context, List<CartItem> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        int itemPaymentIndex = position;

        if (itemPaymentIndex < paymentList.size()) {
            CartItem paymentItem = paymentList.get(itemPaymentIndex);
            if (paymentItem != null) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                holder.nameTextView.setText(paymentItem.getProductName());
                holder.quantity.setText(String.valueOf(paymentItem.getQuantity()));
                holder.sizeTextView.setText(paymentItem.getSize());
                holder.priceTextView.setText("đ"+String.valueOf(decimalFormat.format(paymentItem.getUnitPrice())));
                if(paymentItem.getUrl()==null){
                    Log.d("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", "onBindViewHolder: ");
                    ApiService apiService = ApiService.retrofit.create(ApiService.class);
                    Call<Product_Detail> call1 = apiService.Product_Detail(paymentItem.getProductId());
                    call1.enqueue(new Callback<Product_Detail>() {
                        @Override
                        public void onResponse(Call<Product_Detail> call, Response<Product_Detail> response) {
                            Log.d("ProductDetail", "onResponse: Called");
                            if (response.isSuccessful()) {
                                Product_Detail productDetail = response.body();
                                if (productDetail != null) {
                                    Picasso.get().load(productDetail.getProductUrls().get(0)).into(holder.imageView);
                                }
                            } else {
                                Log.e("ProductDetail", "Error: " + response.message());
                                Log.e("ProductDetail", "Error Code: " + response.code());
                            }

                        }

                        @Override
                        public void onFailure(Call<Product_Detail> call, Throwable t) {
                            Log.e("ProductDetail", "API Call Failed: " + t.getMessage());
                        }
                    });
                }
                else{
                Picasso.get().load(paymentItem.getUrl()).into(holder.imageView);}

            } else {
            }
        }else {
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView nameTextView, priceTextView,quantity;
        private TextView sizeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_pro);
            priceTextView = itemView.findViewById(R.id.price_pro);
            quantity = itemView.findViewById(R.id.quantity_pro);
            imageView = itemView.findViewById(R.id.image_pro);
            sizeTextView = itemView.findViewById(R.id.type_pro);
        }
    }
}
