//package com.androidexam.fashionshop.Fragment.HistoryBuy;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//
//import com.androidexam.fashionshop.Adapter.OrderedItemAdapter;
//import com.androidexam.fashionshop.Api.ApiService;
//import com.androidexam.fashionshop.MainActivity;
//import com.androidexam.fashionshop.Model.ResponseGetOrdered;
//import com.androidexam.fashionshop.Model.ResponseOrder;
//import com.androidexam.fashionshop.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class PurchaseOrderFragment extends Fragment {
//
//
//    private ImageButton back;
//    private RecyclerView rcv;
//    private List<ResponseOrder> listOrdered=new ArrayList<>();
//    private int userId;
//
//
//    public PurchaseOrderFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view=  inflater.inflate(R.layout.fragment_purchase_order, container, false);
//        hideBottomNavigationView();
//        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
//        userId = preferences.getInt("user_id", -1);
//        back = view.findViewById(R.id.back);
//        rcv = view.findViewById(R.id.rcv_order_purchase);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        getOrdered();
//        return view;
//    }
//
//    private void hideBottomNavigationView() {
//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).hideBottomNavigation();
//        }
//    }
//    private void onBackPressed() {
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        fragmentManager.popBackStack();
//    }
//    private void getOrdered(){
//        ApiService.productServiceWithToken.getOrder(userId).enqueue(new Callback<ResponseGetOrdered>() {
//            @Override
//            public void onResponse(Call<ResponseGetOrdered> call, Response<ResponseGetOrdered> response) {
//                if (response.isSuccessful()) {
//                    listOrdered = response.body().getContent();
//                    OrderedItemAdapter orderedItemAdapter = new OrderedItemAdapter(getContext(), listOrdered,requireActivity().getSupportFragmentManager());
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
//                    rcv.setLayoutManager(layoutManager);
//                    rcv.setAdapter(orderedItemAdapter);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseGetOrdered> call, Throwable t) {
//
//            }
//        });
//    }
//
//}