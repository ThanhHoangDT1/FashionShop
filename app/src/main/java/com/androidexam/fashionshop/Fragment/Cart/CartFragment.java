package com.androidexam.fashionshop.Fragment.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Adapter.CartAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Home.DetailProductFragment;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment implements CartAdapter.CartAdapterListener {
    @Override
    public void onCartUpdated() {
        getCartItems();
    }

    public interface OnCartBackPressedListener {
        void onCartFragmentBackPressed();
    }
    private String from;
    private ImageButton btnBack;
    private Button btnBuy;
    private TextView tvDelete, tvTotal;
    private List<Integer> selectedIds = new ArrayList<>();
    private ArrayList<Integer> ListIds;
    private CheckBox cbAll;
    private RecyclerView recyclerView;
    private int userId;
    private int selectedItemCount = 0;
    private List<CartItem> cartItemList;
    private ArrayList<CartItem> cartItemArrayList ;

    private CartAdapter cartAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {

    }


    private CartAdapter.OnItemSelectionChangedListener onItemSelectionChangedListener =
            new CartAdapter.OnItemSelectionChangedListener() {
                @Override
                public void onItemSelectionChanged(CartItem cartItem) {
                    if (cartItem.isSelected()) {
                        selectedIds.add(cartItem.getId());
                    } else {
                        selectedIds.remove(Integer.valueOf(cartItem.getId()));
                    }
                    total(selectedIds);
                    updateDeleteTextViewVisibility();
                }
            };


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);

        if(userId!=-1){

            Bundle bundle = getArguments();
            if (bundle != null) {
                from = bundle.getString("from");
            }
            recyclerView = view.findViewById(R.id.rcv_itemcart);
            btnBuy = view.findViewById(R.id.btn_buy);
            tvTotal = view.findViewById(R.id.total);
            cbAll = view.findViewById(R.id.checkboxall);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            recyclerView.setLayoutManager(layoutManager);
            selectedIds.clear();
            cartItemList = getCartItems();
            cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cartAdapter.selectAllItems(isChecked);

                }
            });

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectedIds.isEmpty()) {
                        onFragmentPayment();
                    }
                }
            });

            cartAdapter = new CartAdapter(requireContext(), cartItemList);
            cartAdapter.setCartAdapterListener(this);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.setOnQuantityChangedListener(new CartAdapter.OnQuantityChangedListener() {
                @Override
                public void onQuantityChanged() {
                    total(selectedIds);
                }
            });

            cartAdapter.setOnItemSelectionChangedListener(onItemSelectionChangedListener);

            tvDelete = view.findViewById(R.id.delete);
            tvDelete.setOnClickListener(v -> onDeleteButtonClick());



            btnBack = view.findViewById(R.id.back_button_car);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        else {
            navigateToLoginFragment();

        }

        return view;
    }

    private void onFragmentPayment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        cartItemArrayList = new ArrayList<>(cartItemList);
        Log.d("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH",String.valueOf(selectedIds.size()));
        ListIds = new ArrayList<>(selectedIds);
        PayFragment payFragment = new PayFragment();
        Bundle args = new Bundle();
        args.putString("from","cart");
        args.putParcelableArrayList("cartItemList", cartItemArrayList);
        args.putIntegerArrayList("listId", ListIds);
        payFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, payFragment)
                .addToBackStack(null)
                .commit();
    }


    private void onDeleteButtonClick() {
        if (!selectedIds.isEmpty()) {


            ApiService.productServiceWithToken.delete(userId, selectedIds).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Void deleteSuccess = response.body();


                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            SharedData.setLoadingFirstTime(true);
                            getCartItems();
                            resetSelection();
                    } else {

                        Toast.makeText(getContext(), "1.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Chọn ít nhất một mục để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDeleteTextViewVisibility() {

        if (selectedItemCount > 0) {
            tvDelete.setVisibility(View.GONE);
        } else {
            tvDelete.setVisibility(View.VISIBLE);
        }
    }

    private void resetSelection() {
        selectedItemCount = 0;
        cartAdapter.resetSelection();
        updateDeleteTextViewVisibility();
    }

    private List<CartItem> getCartItems() {
        SharedData.setLoadingFirstTime(true);
        List<CartItem> cartItems = new ArrayList<>();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        ApiService.productServiceWithToken.getCart(userId).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    List<CartItem> newCart = response.body();
                    if (newCart != null && !newCart.isEmpty()) {
                        cartItemList.clear();
                        cartItemList.addAll(newCart);
                        cartAdapter.notifyDataSetChanged();
                    }else{
                        cartItemList.clear();
                        cartAdapter.notifyDataSetChanged();

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

        return cartItems;
    }

    private void onBackPressed() {

        if ("productDetail".equals(from)) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        else {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                HomeFragment homeFragment = new HomeFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit();

        }
    }

    private void total(List<Integer> listitem) {
        long S = 0;
        for (int i : listitem
        ) {
            for (CartItem j : cartItemList
            ) {
                if (j.getId() == i) {
                    S = S + j.getDisPrice() * j.getQuantity();
                }

            }

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTotal.setText("đ"+String.valueOf(decimalFormat.format(S)));
    }
    private void navigateToLoginFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commit();
    }
}