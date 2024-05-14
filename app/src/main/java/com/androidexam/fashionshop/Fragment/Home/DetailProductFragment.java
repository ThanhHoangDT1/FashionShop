package com.androidexam.fashionshop.Fragment.Home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Cart.CartFragment;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductFragment extends Fragment {

    public static DetailProductFragment newInstance(int productId) {
        DetailProductFragment fragment = new DetailProductFragment();
        Bundle args = new Bundle();
        args.putInt("product_id", productId);
        fragment.setArguments(args);
        return fragment;
    }

    private   List<CartItem> cartItemList=new ArrayList<>();
    private ArrayList<CartItem> cartItemArrayList ;
    private ArrayList<Integer> ListIds;
    private List<Integer> selectedIds = new ArrayList<>();
    private int userId;
    private int productId;
    private String selectedSize;
    private AlertDialog alertDialog;
    private int currentQuantity = 1;
    private ViewPager viewPager;
    private ImageButton btnBack;
    private ImageButton btnCart;
    private ImageButton btnHome;
    private ImageButton btnChat;
    private ImageButton btnAddToCart;
    private Button btnBuyNow;
    private TextView cart_account;
    private FragmentManager childFragmentManager;
    private DetailContentFragment detailContentFragment;

    private int selectedQuantity;
    private Product_Detail productDetail;

    public DetailProductFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        hideBottomNavigationView();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        viewPager = view.findViewById(R.id.viewPager);
        btnBack = view.findViewById(R.id.back_button);
        btnCart = view.findViewById(R.id.cart_button);
        cart_account = view.findViewById(R.id.cart_count);
        btnAddToCart = view.findViewById(R.id.add_to_cart_button);
        btnBuyNow = view.findViewById(R.id.buy_now_button);
        btnHome = view.findViewById(R.id.home);
        btnChat = view.findViewById(R.id.chat_button);
        childFragmentManager = getChildFragmentManager();
        detailContentFragment = new DetailContentFragment();
        if(userId!=-1){
            getCartAccount();
        }
        cartItemList.clear();
        childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_detail, detailContentFragment)
                .commit();

        // Lấy ID sản phẩm từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getInt("product_id", -1);
            if (productId != -1) {
                getProductDetail(productId);
            } else {
            }
        }
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCartFragment();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHome();
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    showAddToCartDialog(1);
                } else {
                    navigateToLoginFragment();
                }

            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      showAddToCartDialog();

                if (userId != -1) {
                    showAddToCartDialog(0);
                } else {
                    navigateToLoginFragment();
                }
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHome();
            }
        });
        return view;
    }

    private void showAddToCartDialog(int x) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_cart, null);
        builder.setView(dialogView);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        editTextQuantity.setText(String.valueOf(currentQuantity));

        ImageButton buttonIncrease = dialogView.findViewById(R.id.buttonIncrease);
        ImageButton buttonDecrease = dialogView.findViewById(R.id.buttonDecrease);
        ImageView imageDialog = dialogView.findViewById(R.id.image_dialog);
        Picasso.get().load(productDetail.getProductUrls().get(0)).into(imageDialog);
        TextView productPriceOld = dialogView.findViewById(R.id.product_price_old);
        String originalPrice = String.valueOf(decimalFormat.format(productDetail.getPrice()));
        TextView productPrice = dialogView.findViewById(R.id.product_price);
        SpannableString spannableString = new SpannableString(originalPrice);
        spannableString.setSpan(new StrikethroughSpan(), 0, originalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        productPriceOld.setText(spannableString);
        productPrice.setText(String.valueOf("đ" + decimalFormat.format(productDetail.getPrice_promote())));
        TextView quantityTextView = dialogView.findViewById(R.id.quatity);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuantity++;
                editTextQuantity.setText(String.valueOf(currentQuantity));
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity > 1) {
                    currentQuantity--;
                    editTextQuantity.setText(String.valueOf(currentQuantity));
                }
            }
        });

        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);
        Button buttonConfirmBuyNow = dialogView.findViewById(R.id.btnBuyNow);
        List<String> sizeList = productDetail.getSizeNames();
        alertDialog  = builder.create();

        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
            Spinner spinnerSize = alertDialog.findViewById(R.id.spinnerSize);
            if (spinnerSize != null) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizeList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSize.setAdapter(spinnerAdapter);
                spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        selectedSize = sizeList.get(position);
                         selectedQuantity = Integer.parseInt(productDetail.getSizeQuantity().get(position));
                        quantityTextView.setText(String.valueOf(selectedQuantity));
                        if(selectedQuantity==0){
                            editTextQuantity.setText(String.valueOf(0));
                        }
                        else {
                            editTextQuantity.setText(String.valueOf(1));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }
            Window window = alertDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.gravity = Gravity.BOTTOM;
                window.setAttributes(layoutParams);
            }
            editTextQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        int enteredQuantity = Integer.parseInt(editable.toString());
                        currentQuantity = enteredQuantity;
                        if (enteredQuantity > Integer.parseInt(quantityTextView.getText().toString())) {
                            //     Toast.makeText(DetailProductFragment.this, "Số lượng không được lớn hơn " + remainingQuantity, Toast.LENGTH_SHORT).show();
                            editTextQuantity.setText(quantityTextView.getText().toString());
                            currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                            return;
                        }
                        if (String.valueOf(enteredQuantity).length() > String.valueOf(quantityTextView).length()) {
                            editTextQuantity.setText(quantityTextView.getText().toString());
                            currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                        }
                    }
                }
            });
        }
        if(x==0){
            buttonConfirm.setVisibility(View.VISIBLE);
            buttonConfirmBuyNow.setVisibility(View.GONE);
            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());

                    if(quantity>0){
                        CartItem cart = new CartItem(quantity, productId, selectedSize);

                        ApiService.productServiceWithToken.addProduct(userId, cart).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    ResponseBody addedCart = response.body();
                                    Toast.makeText(getContext(), "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "khong them thanh cong. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "ko koko. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                                //alertDialog.dismiss();
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Sản phâm đã hết hàng. Vui lòng chọn sản phẩm khác.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else if (x==1) {
            buttonConfirm.setVisibility(View.GONE);
            buttonConfirmBuyNow.setVisibility(View.VISIBLE);
            buttonConfirmBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());

                    if(quantity>0){
                        CartItem cart = new CartItem(quantity, productId, selectedSize);
                        cart.setSizeProduct(productDetail.getSizeNames());
                        cart.setProductName(productDetail.getProductName());
                        cart.setDisPrice(productDetail.getPrice_promote());
                        cart.setUrl(productDetail.getProductUrls().get(0));
                        cart.setRemain(selectedQuantity);
                        cart.setUnitPrice(productDetail.getPrice());

                        cartItemList.add(cart);
                        Log.d("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", String.valueOf(cartItemList.size()));
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        cartItemArrayList = new ArrayList<>(cartItemList);
                        ListIds = new ArrayList<>(selectedIds);
                        Log.d("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", String.valueOf(selectedIds.size()));
                        ListIds.add(0);
                        Log.d("MMMMMMMMMMMMMMMMMMMMMMMM", String.valueOf(ListIds.size()));
                        PayFragment payFragment = new PayFragment();
                        Bundle args = new Bundle();
                        args.putString("from","productDetail");
                        args.putParcelableArrayList("cartItemList", cartItemArrayList);
                        args.putIntegerArrayList("listId", ListIds);
                        payFragment.setArguments(args);
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, payFragment)
                                .addToBackStack(null)
                                .commit();
                        alertDialog.dismiss();
                    }
                    else {
                        Toast.makeText(getContext(), "Sản phâm đã hết hàng. Vui lòng chọn sản phẩm khác.", Toast.LENGTH_SHORT).show();
                    }
                    }




            });
        }

    }

    private void getProductDetail(int productId) {
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<Product_Detail> call = apiService.Product_Detail(productId);
        call.enqueue(new Callback<Product_Detail>() {
            @Override
            public void onResponse(Call<Product_Detail> call, Response<Product_Detail> response) {
                if (response.isSuccessful()) {
                    Product_Detail productDetail = response.body();
                    Log.d("CHI TIET", "onResponse: "+response.body().getProductName());
                    Log.d("CHI TIET", "des: "+productDetail.getDescription()+" url  "+productDetail.getProductUrls()+"id"+productDetail.getProductId()+productDetail.getProductName());
                    if (productDetail != null) {
                        setProductDetail(productDetail);
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

    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void onHome() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }

    private void displayProductDetails() {
        if (productDetail != null) {
            if (detailContentFragment != null) {
                detailContentFragment.setProductDetail(productDetail);
            }
        }
    }

    public void setProductDetail(Product_Detail productDetail) {
        this.productDetail = productDetail;
        displayProductDetails();
    }

    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }

    private void navigateToLoginFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt("product_id", productId);
        loginFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commit();
    }
    private void navigateToCartFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        CartFragment cartFragment = new CartFragment();

        Bundle args = new Bundle();
        args.putString("from","productDetail");

        cartFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, cartFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public  void getCartAccount(){
        ApiService.productServiceWithToken.getCart(userId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    List<CartItem> newCart = response.body();
                    int i = newCart.size();
                    if (i!=0){
                        cart_account.setVisibility(View.VISIBLE);
                        cart_account.setText(String.valueOf(i));
                    }
                    else {
                        cart_account.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("API Error", "Response is not successful");
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                String errorMessage = "API Call Failed: " + t.getMessage();
                Log.e("API Error", errorMessage);
            }
        });
    }
}

