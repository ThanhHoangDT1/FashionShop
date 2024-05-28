package com.androidexam.fashionshop.Fragment.HistoryBuy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidexam.fashionshop.Decoration.MyInterface;
import com.androidexam.fashionshop.Fragment.Individual.AddressFragment;
import com.androidexam.fashionshop.Fragment.Pay.PayContentFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;

public class OrderedDetailFragment extends Fragment  {

    private FragmentManager childFragmentManager;
    private OrderedDetailContentFragment orderedDetailContentFragment;
    private ArrayList<OrderItem> orderItemArrayList ;
    private ImageButton back;
    private AddressItem addressItemChosen;
    private ArrayList<Integer> listIds;
    ArrayList<CartItem> listBuy;
    private String orderId;
    public OrderedDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ordered_detail, container, false);

        back = view.findViewById(R.id.back_view_history_ordered_button);
        orderedDetailContentFragment = new OrderedDetailContentFragment();
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getString("orderedId","");
        }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            childFragmentManager = getChildFragmentManager();
            Bundle args = new Bundle();
            args.putString("orderedId", orderId);
            //args.putString("status",);
            orderedDetailContentFragment.setArguments(args);
            childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_ordered_detail, orderedDetailContentFragment)
                    .addToBackStack(null)
                    .commit();




        return view;
    }
    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}