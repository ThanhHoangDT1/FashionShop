package com.androidexam.fashionshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;
import com.androidexam.fashionshop.Fragment.Cart.SharedData;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private OnItemSelectionChangedListener onItemSelectionChangedListener;
    private OnItemUpdatedListener onItemUpdatedListener;
    private OnQuantityChangedListener quantityChangedListener;
    public interface CartAdapterListener {
        void onCartUpdated();
    }
    private CartAdapterListener listener;

    public void setCartAdapterListener(CartAdapterListener listener) {
        this.listener = listener;
    }


    private TextWatcher textWatcher;
    private List<CartItem> cartItemList;
    private Context context;
    private AlertDialog sizeDialog; // Khai báo biến dialog ở mức độ lớp

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (cartItemList == null || position < 0 || position >= cartItemList.size()) {
            return;
        }

        CartItem cartItem = cartItemList.get(position);

        if (cartItem == null || cartItem.getProductName() == null || cartItem.getProductName().isEmpty()) {
            if (cartItem != null && cartItem.isApiCallInProgress()) {
                return;
            }

            if (cartItem != null) {
                cartItem.setApiCallInProgress(true);
                Log.d("ID PR", String.valueOf(cartItem.getProductId()));
                ApiService apiService = ApiService.retrofit.create(ApiService.class);
                Call<Product_Detail> call = apiService.Product_Detail(cartItem.getProductId());
                call.enqueue(new Callback<Product_Detail>() {
                    @Override
                    public void onResponse(Call<Product_Detail> call, Response<Product_Detail> response) {
                        if (cartItem != null) {
                            cartItem.setApiCallInProgress(false);

                            if (response.isSuccessful()) {
                                Product_Detail productDetail = response.body();
                                if (productDetail != null) {

                                    cartItem.setSizeProduct(productDetail.getSizeNames());
                                    cartItem.setProductName(productDetail.getProductName());
                                    cartItem.setDisPrice(productDetail.getPrice_promote());
                                    cartItem.setUrl(productDetail.getProductUrls().get(0));
                                    int positionOfM = productDetail.getSizeNames().indexOf(cartItem.getSize());
                                    cartItem.setRemain(Integer.parseInt(productDetail.getSizeQuantity().get(positionOfM)));
                                    cartItem.setUnitPrice(productDetail.getPrice());
                                    notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product_Detail> call, Throwable t) {
                        Toast.makeText(context, "API Call Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        if (cartItem != null) {
                            cartItem.setApiCallInProgress(false);
                        }
                    }
                });
            }
        }
        holder.bind(cartItem, this);
    }

    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener listener) {
        this.onItemSelectionChangedListener = listener;
    }

    public void setItemSelection(int position, boolean isSelected) {
        cartItemList.get(position).setSelected(isSelected);
        notifyDataSetChanged();
    }

    public void resetSelection() {
        for (CartItem item : cartItemList) {
            item.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void selectAllItems(boolean isSelected) {
        for (CartItem item : cartItemList) {
            item.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    public void updateCartItem(int position, CartItem updatedCartItem) {
        if (position >= 0 && position < cartItemList.size()) {
            cartItemList.set(position, updatedCartItem);
            notifyItemChanged(position);
        }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private int currentQuantity;
        private int userId;
        private CheckBox checkBox;
        private String selectedSize;
        private ImageView imageView;
        private TextView nameTextView, priceTextView, priceUnitTextview;
        private EditText quantityEdt;
        private ImageButton add, sub;
        private TextView sizeTextView;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            SharedPreferences preferences = context.getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
            userId = preferences.getInt("user_id", -1);
            checkBox = itemView.findViewById(R.id.select);
            sizeTextView = itemView.findViewById(R.id.type);
            imageView = itemView.findViewById(R.id.image);
            nameTextView = itemView.findViewById(R.id.nameproduct);
            priceTextView = itemView.findViewById(R.id.product_price_cart);
            priceUnitTextview = itemView.findViewById(R.id.product_price_old_cart);
            quantityEdt = itemView.findViewById(R.id.editTextQuantity);
            add = itemView.findViewById(R.id.buttonIncreaseCart);
            sub = itemView.findViewById(R.id.buttonDecreaseCart);
        }

        public void bind(CartItem cartItem, CartAdapter cartAdapter) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            nameTextView.setText(cartItem.getProductName());
            priceTextView.setText("đ" + decimalFormat.format(cartItem.getDisPrice()));
            checkBox.setChecked(cartItem.isSelected());
            String originalPrice = String.valueOf(decimalFormat.format(cartItem.getUnitPrice()));
            SpannableString spannableString = new SpannableString(originalPrice);
            spannableString.setSpan(new StrikethroughSpan(), 0, originalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceUnitTextview.setText(spannableString);
            quantityEdt.setText(String.valueOf(cartItem.getQuantity()));
            Picasso.get().load(cartItem.getUrl()).into(imageView);

            currentQuantity = Integer.parseInt(quantityEdt.getText().toString());
            List<String> sizeList = cartItem.getSizeProduct();

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cartItem.setSelected(isChecked);
                onItemSelectionChangedListener.onItemSelectionChanged(cartItem);
            });

            if (sizeList != null && sizeTextView != null) {
                String sizeCurrent = cartItem.getSize();
                sizeTextView.setText(sizeCurrent);
                sizeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSizeDialog(cartItem,cartAdapter);
                        //notifyDataSetChanged();
                    }
                });
            }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuantity++;
                    SharedData.setLoadingFirstTime(false);
                    // Remove TextWatcher to prevent triggering afterTextChanged unnecessarily
                    quantityEdt.removeTextChangedListener(textWatcher);
                    quantityEdt.setText(String.valueOf(currentQuantity));
                    // Re-attach TextWatcher
                    quantityEdt.addTextChangedListener(textWatcher);
                }
            });

            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentQuantity > 1) {
                        SharedData.setLoadingFirstTime(false);
                        currentQuantity--;
                        // Remove TextWatcher to prevent triggering afterTextChanged unnecessarily
                        quantityEdt.removeTextChangedListener(textWatcher);
                        quantityEdt.setText(String.valueOf(currentQuantity));
                        // Re-attach TextWatcher
                        quantityEdt.addTextChangedListener(textWatcher);
                    }
                }
            });

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Nothing to do here
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String inputString = s.toString();
                    if (!inputString.isEmpty()) {
                        try {
                            cartItem.setQuantity(Integer.parseInt(inputString));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if (quantityChangedListener != null) {
                        quantityChangedListener.onQuantityChanged();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d("CART", "afterTextChanged called");
                    if (!editable.toString().isEmpty()) {
                        int enteredQuantity = Integer.parseInt(editable.toString());
                        currentQuantity = enteredQuantity;

                        if (enteredQuantity > cartItem.getRemain()) {
                            // Remove TextWatcher to prevent triggering afterTextChanged unnecessarily
                            quantityEdt.removeTextChangedListener(this);
                            quantityEdt.setText(String.valueOf(cartItem.getRemain()));
                            currentQuantity = cartItem.getRemain();
                            // Re-attach TextWatcher
                            quantityEdt.addTextChangedListener(this);
                            return;
                        }

                        if (String.valueOf(enteredQuantity).length() > String.valueOf(cartItem.getRemain()).length()) {
                            // Remove TextWatcher to prevent triggering afterTextChanged unnecessarily
                            quantityEdt.removeTextChangedListener(this);
                            quantityEdt.setText(String.valueOf(cartItem.getRemain()));
                            currentQuantity = cartItem.getRemain();
                            // Re-attach TextWatcher
                            quantityEdt.addTextChangedListener(this);
                        }
                        boolean loadingFirstTime = SharedData.isLoadingFirstTime();
                        if (loadingFirstTime != true) {
                            int quantity = Integer.parseInt(quantityEdt.getText().toString());
                            CartItem updatedCartItem = new CartItem(cartItem.getId(), quantity, cartItem.getProductId(), cartItem.getSize());
                            Log.d("CART", "userId: : "+ userId+"id: "+cartItem.getId());
                            ApiService.productServiceWithToken.editProduct(userId, cartItem.getId(), updatedCartItem).enqueue(new Callback<CartItem>() {
                                @Override
                                public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                                    if (response.isSuccessful()) {
                                        CartItem addedCart = response.body();
                                        Log.d("long", "thanh cong");
                                    } else {
                                        Log.d("long", "that bai");
                                        Toast.makeText(context, "Thêm không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CartItem> call, Throwable t) {
                                    Toast.makeText(context, "Có lỗi xảy ra. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            SharedData.setLoadingFirstTime(true);
                        }
                    }
                }
            };

// Attach TextWatcher to quantityEdt
            quantityEdt.addTextChangedListener(textWatcher);

        }
        private void openSizeDialog(CartItem cartItem, CartAdapter cartAdapter) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.sizedialog, null);
            builder.setView(dialogView).setTitle("Edit Size");

            ListView listView = dialogView.findViewById(R.id.listViewSize);
            List<String> sizeList = cartItem.getSizeProduct();
            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, sizeList);
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedSize = sizeList.get(position);

                    // Your size selection logic here
                    sizeTextView.setText(selectedSize);

                    // Gửi API và cập nhật danh sách
                    int quantity = Integer.parseInt(quantityEdt.getText().toString());
                    CartItem updatedCartItem = new CartItem();
                    updatedCartItem.setSize(selectedSize);
                    updatedCartItem.setId(cartItem.getId());
                    updatedCartItem.setProductId(cartItem.getProductId());
                    updatedCartItem.setQuantity(quantity);

                    ApiService.productServiceWithToken.editProduct(userId, cartItem.getId(), updatedCartItem).enqueue(new Callback<CartItem>() {
                        @Override
                        public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                            if (response.isSuccessful()) {
                                CartItem cartItem1 = response.body();
                                if (cartItem1 != null && cartAdapter != null) {
                                    cartAdapter.updateCartItem(getAdapterPosition(), updatedCartItem);

                                    notifyDataSetChanged();
                                    if (listener != null) {
                                        listener.onCartUpdated();
                                    }

                                }
                                // Đóng dialog khi API thành công
                                if (sizeDialog != null && sizeDialog.isShowing()) {
                                    sizeDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(context, "Thay đổi size không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<CartItem> call, Throwable t) {
                            Toast.makeText(context, "Lỗi kết nối. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            listView.setOnItemClickListener(itemClickListener);
            listView.setAdapter(sizeAdapter);

            sizeDialog = builder.create();
            sizeDialog.getWindow().setLayout(300, 400);
            sizeDialog.show();
        }

    }

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(CartItem cartItem);
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public interface OnItemUpdatedListener {
        void onItemUpdated();
    }

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.quantityChangedListener = listener;
    }

    public void setOnItemUpdatedListener(OnItemUpdatedListener listener) {
        this.onItemUpdatedListener = listener;
    }
    private int calculateRecyclerViewHeight(int itemCount) {
        // Tính toán chiều cao dựa trên kích thước item và số lượng item
        int itemHeight = calculateItemHeight();
        int totalHeight = itemHeight * itemCount;
        return totalHeight;
    }
    private int calculateItemHeight() {
        // Tính toán chiều cao của mỗi item, có thể cần điều chỉnh theo yêu cầu của bạn
        // Ví dụ: return chiều cao cố định, hoặc tính toán dựa trên các yếu tố khác
        return 300; // Đây chỉ là một giả định, bạn cần điều chỉnh nó theo yêu cầu thực tế
    }
}

