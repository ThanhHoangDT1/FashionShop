package com.androidexam.fashionshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.HistoryBuy.NotYetRatedFragment;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderProductItemAdapter extends RecyclerView.Adapter<OrderProductItemAdapter.ViewHolder> {
    public interface OnCommentSentListener {
        void onCommentSent();
    }

    private OnCommentSentListener commentSentListener;

    public void setOnCommentSentListener(OnCommentSentListener listener) {
        this.commentSentListener = listener;
    }

    private void notifyCommentSent() {
        if (commentSentListener != null) {
            commentSentListener.onCommentSent();
        }
    }
    private List<OrderItem> OrderProductList;
    private Context context;
    private int userId;

    public OrderProductItemAdapter(Context context, List<OrderItem> OrderProductList) {
        this.context = context;
        this.OrderProductList = OrderProductList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductItemAdapter.ViewHolder holder, int position) {
        int itemPaymentIndex = position;
        SharedPreferences preferences = context.getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        if (itemPaymentIndex < OrderProductList.size()) {
            OrderItem paymentItem = OrderProductList.get(itemPaymentIndex);
            if (paymentItem != null) {

                ApiService apiService = ApiService.retrofit.create(ApiService.class);
                Call<Product_Detail> call1 = apiService.Product_Detail(paymentItem.getProductId());
                call1.enqueue(new Callback<Product_Detail>() {
                    @Override
                    public void onResponse(Call<Product_Detail> call, Response<Product_Detail> response) {
                        Log.d("ProductDetail", "onResponse: Called");
                        if (response.isSuccessful()) {
                            Product_Detail productDetail = response.body();
                            if (productDetail != null) {
                                holder.nameTextView.setText(productDetail.getProductName());
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
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
              if (paymentItem.isRate()==true){
                  holder.rate.setVisibility(View.GONE);

              }
              else{
                  holder.rate.setVisibility(View.VISIBLE);
                  holder.rate.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Log.d("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV", String.valueOf(paymentItem.getId()));
                          OnComment(paymentItem.getId(),paymentItem.getProductId());
                      }
                  });
              }
                holder.quantity.setText(String.valueOf(paymentItem.getQuantity()));
                holder.sizeTextView.setText(paymentItem.getSizeType());
                holder.priceTextView.setText("đ"+String.valueOf(decimalFormat.format(paymentItem.getUnitPrice())));




            }


        }else {
        }



    }
    private void OnComment(int orderItemId, int productId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);
        builder.setView(dialogView);

        EditText edtContentComment = dialogView.findViewById(R.id.commentEditText);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        Button btnSent= dialogView.findViewById(R.id.btn_sent);

        AlertDialog alertDialog = builder.create();

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int rating = (int)ratingBar.getRating();
                String comment = edtContentComment.getText().toString().trim();

                if(rating>0 && !comment.isEmpty()){
                    ApiService.productServiceWithToken.sentComment(rating, orderItemId,productId, userId, comment)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Gởi đánh giá thành công.", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss(); // Đóng dialog sau khi gửi thành công
                                        notifyCommentSent();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // Xử lý lỗi khi gọi API
                                }
                            });

                } else if (rating<=0) {
                    Toast.makeText(context, "Vui lòng chọn sao trước khi gởi đánh giá.", Toast.LENGTH_SHORT).show();

                } else if (comment.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập nội dung đánh giá trước khi gởi.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return OrderProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView nameTextView, priceTextView,quantity;
        private TextView sizeTextView, rate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_pro);
            priceTextView = itemView.findViewById(R.id.price_pro);
            quantity = itemView.findViewById(R.id.quantity_pro);
            imageView = itemView.findViewById(R.id.image_pro);
            sizeTextView = itemView.findViewById(R.id.type_pro);
            rate = itemView.findViewById(R.id.rate);
        }
    }
}
