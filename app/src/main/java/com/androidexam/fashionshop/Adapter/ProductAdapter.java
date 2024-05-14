package com.androidexam.fashionshop.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Fragment.Home.DetailProductFragment;
import com.androidexam.fashionshop.Model.Product;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private FragmentManager fragmentManager;

    public ProductAdapter(List<Product> productList, FragmentManager fragmentManager) {
        this.productList = productList;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int productIndex = position;

        if (productIndex < productList.size()) {
            Product product = productList.get(productIndex);
            if (product != null) {
                // Set data for your product item here
                holder.productName.setText(product.getProductName());
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


                if(product.getPrice_promote()==product.getPrice()){
                    holder.productPrice.setVisibility(View.GONE);
                    holder.d.setVisibility(View.GONE);
                    holder.getProductPriceOld.setText("đ" +decimalFormat.format(product.getPrice_promote()));
                    holder.getProductPriceOld.setTextColor(Color.RED);
                    holder.getProductPriceOld.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    Log.d("GIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",product.getPrice_promote()+"===="+ product.getPrice());
                }
                else {
                    holder.d.setVisibility(View.VISIBLE);
                    holder.productPrice.setVisibility(View.VISIBLE);
                    holder.productPrice.setText("đ" +decimalFormat.format(product.getPrice_promote()));
                    holder.getProductPriceOld.setVisibility(View.VISIBLE);
                    holder.getProductPriceOld.setTextColor(Color.GRAY);
                    holder.getProductPriceOld.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                    String originalPrice = String.valueOf(decimalFormat.format(product.getPrice()));
                    SpannableString spannableString = new SpannableString(originalPrice);
                    spannableString.setSpan(new StrikethroughSpan(), 0, originalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.getProductPriceOld.setText(spannableString);
                Log.d("GIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",product.getPrice_promote()+"===="+ product.getPrice());

                }




                holder.soldQuantity.setText(String.valueOf(product.getQuantitySold()));
                if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
                    String imageUrl1 = product.getProductImage().get(0);
                    Log.d("HUHU", "onBindViewHolder: "+imageUrl1);
                    Picasso.get()
                            .load(imageUrl1)
                            .into(holder.productImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // Hình ảnh đã được tải thành công
                                    System.out.println("Image loaded successfully");
                                    Log.d("loading image", "Image loaded successfully: ");
                                }

                                @Override
                                public void onError(Exception e) {
                                    // Lỗi khi tải hình ảnh
                                    Log.d("Error loading image", "onError: "+e.getMessage());
                                    System.out.println("Error loading image: " + e.getMessage());
                                }
                            });
//                    Picasso.get().load(imageUrl1).into(holder.productImage);
                }

            } else {

            }
        }else {
        }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int productId = productList.get(productIndex).getId();
                        String productNAme = productList.get(productIndex).getProductName();
                        // Create DetailProductFragment and pass product ID
                        Log.d("Idproduct", String.valueOf(productId));
                        DetailProductFragment detailFragment = new DetailProductFragment();
                        Bundle args = new Bundle();
                        args.putInt("product_id", productId);
                        detailFragment.setArguments(args);

                        // Start a Fragment transaction to replace the current Fragment
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, detailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView soldQuantity;
        TextView productPrice;
        TextView getProductPriceOld,d;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            soldQuantity = itemView.findViewById(R.id.sold_quantity);
            getProductPriceOld = itemView.findViewById(R.id.product_price_old);
            d =itemView.findViewById(R.id.d);
        }
    }
}
