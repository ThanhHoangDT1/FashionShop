package com.androidexam.fashionshop.Fragment.HistoryBuy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidexam.fashionshop.Adapter.OrderedItemAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.ResponseGetOrdered;
import com.androidexam.fashionshop.Model.ResponseOrder;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InTransitFragment extends Fragment {


    private int userId;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<OrderItem> listOrderItem=new ArrayList<>();
    private List<ResponseOrder> listOrdered;

    public InTransitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_in_transit, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        recyclerView = view.findViewById(R.id.rcv_intransit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        ViewOrderItemRated();
        // OrderProductItemAdapter orderedItemAdapter = new OrderProductItemAdapter(getContext(), listOrderItem);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);



        return view;
    }

    public void ViewOrderItemRated() {

        ApiService.productServiceWithToken.getOrder(userId,"IN_TRANSIT").enqueue(new Callback<ResponseGetOrdered>() {
            @Override
            public void onResponse(Call<ResponseGetOrdered> call, Response<ResponseGetOrdered> response) {
                if (response.isSuccessful()) {
                    listOrdered = response.body().getContent();
                    OrderedItemAdapter orderedItemAdapter = new OrderedItemAdapter(getContext(), listOrdered,requireActivity().getSupportFragmentManager(),"transported");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(orderedItemAdapter);
                }

            }

            @Override
            public void onFailure(Call<ResponseGetOrdered> call, Throwable t) {

            }
        });

    }


}

