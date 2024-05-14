package com.androidexam.fashionshop.Fragment.HistoryBuy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.androidexam.fashionshop.Adapter.OrderProductItemAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.ResponseGetOrderItemDelivered;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotYetRatedFragment extends Fragment implements OrderProductItemAdapter.OnCommentSentListener{


    private int userId;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private  List<OrderItem> listOrderItem=new ArrayList<>();

    public NotYetRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_rated, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        recyclerView = view.findViewById(R.id.rcv_rate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        ViewOrderItemRated();
       // OrderProductItemAdapter orderedItemAdapter = new OrderProductItemAdapter(getContext(), listOrderItem);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);



        return view;
    }
    @Override
    public void onCommentSent() {
        ViewOrderItemRated();
    }
    public void ViewOrderItemRated() {

        ApiService.productServiceWithToken.getOrderItemDelivered(userId,false).enqueue(new Callback<ResponseGetOrderItemDelivered>() {
            @Override
            public void onResponse(Call<ResponseGetOrderItemDelivered> call, Response<ResponseGetOrderItemDelivered> response) {
                if(response.isSuccessful()){
                    listOrderItem =response.body().getContent();
                    OrderProductItemAdapter orderedItemAdapter = new OrderProductItemAdapter(getContext(), listOrderItem);
                    orderedItemAdapter.setOnCommentSentListener(NotYetRatedFragment.this);
                    recyclerView.setAdapter(orderedItemAdapter);

                }

            }

            @Override
            public void onFailure(Call<ResponseGetOrderItemDelivered> call, Throwable t) {

            }
        });
    }


}