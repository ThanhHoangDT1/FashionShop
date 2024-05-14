package com.androidexam.fashionshop.Fragment.Pay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidexam.fashionshop.Decoration.MyInterface;
import com.androidexam.fashionshop.Fragment.Cart.CartFragment;
import com.androidexam.fashionshop.Fragment.Home.DetailProductFragment;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Fragment.Individual.AddressFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;

public class PayFragment extends Fragment implements MyInterface.OnFunctionXListener {

    private FragmentManager childFragmentManager;
    private PayContentFragment payContentFragment;
    private ArrayList<CartItem> cartItemArrayList ;
    private ImageButton back;
    private AddressItem addressItemChosen;
    private ArrayList<Integer> listIds=new ArrayList<>();
    private TextView btnOrder,totalPay;
    private String method;
    private String from;
    private int proId;
    ArrayList<CartItem> listBuy = new ArrayList<>();
    public PayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pay, container, false);

        back = view.findViewById(R.id.back_button);
        btnOrder = view.findViewById(R.id.buy_now_button);
        payContentFragment = new PayContentFragment();
        payContentFragment.setOnFunctionXListener(this);
        Log.d("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", String.valueOf(listBuy.size()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            cartItemArrayList = bundle.getParcelableArrayList("cartItemList");
            listIds = bundle.getIntegerArrayList("listId");
            addressItemChosen=(AddressItem)bundle.getSerializable("addressChosen");
            method =bundle.getString("method");
            from = bundle.getString("from");
            proId=cartItemArrayList.get(0).getProductId();
            if(method!=null){Log.d("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMEEETHODDDD", method);}
            if(listBuy!=null){
                listBuy.clear();
            }
            for (Integer id : listIds) {
                Log.d("ListID", String.valueOf(id));
                for (CartItem cartItem : cartItemArrayList) {
                    if (cartItem.getId() == id) {
                        Log.d("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", "vvvvvvvvvvvvvvvvvvvvooooooooooooooooooooooo");
                        listBuy.add(cartItem);

                    }

                }
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        listBuy.clear();

                    onBackPressed();
                }
            });
            childFragmentManager = getChildFragmentManager();
            Bundle args = new Bundle();
            Log.d("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", String.valueOf(listBuy.size()));
            args.putParcelableArrayList("listBuyItem", listBuy);
            args.putString("method",method);
            args.putSerializable("addressChoose", addressItemChosen);
            payContentFragment.setArguments(args);
            childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_detail, payContentFragment)
                    .addToBackStack("payment1")
                    .commit();

        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payContentFragment.Pay();
            }
        });

        return view;
    }
    private void onBackPressed() {
        if ("cart".equals(from)) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            CartFragment cartFragment = new CartFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, cartFragment)
                    .commit();
        } else if ("productDetail".equals(from)) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            DetailProductFragment detailProductFragment = new DetailProductFragment();
            Bundle args = new Bundle();
            args.putInt("product_id", proId);
            detailProductFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailProductFragment)
                    .commit();
        }

        else {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            CartFragment cartFragment = new CartFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, cartFragment)
                    .commit();
        }
    }
    @Override
    public void onFunctionX() {
        openAddressfromPayment();
    }
private void openAddressfromPayment() {

    AddressFragment addressFragment = new AddressFragment();
    String method1 = payContentFragment.getMethod();
    Bundle args = new Bundle();
    args.putInt("id", 1);
    args.putString("from",from);
    args.putString("method",method1);
    args.putParcelableArrayList("cartItemList", cartItemArrayList);
    args.putIntegerArrayList("listId", listIds);
    addressFragment.setArguments(args);
    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, addressFragment);
    fragmentTransaction.addToBackStack("payment2");
    fragmentTransaction.commit();
}
}