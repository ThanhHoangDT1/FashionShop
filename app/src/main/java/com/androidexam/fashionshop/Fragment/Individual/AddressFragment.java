package com.androidexam.fashionshop.Fragment.Individual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Adapter.AddressAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFragment extends Fragment {

    private ImageButton back;
    private int userId;
    private String from;
    private int id=0;
    private String method;
    private LinearLayout addAddress;
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<AddressItem> addressItemList;
    private ArrayList<CartItem> cartItemArrayList ;
    private ArrayList<Integer> listIds;

    public AddressFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressItemList = new ArrayList<>();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_fragment, container, false);
        hideBottomNavigationView();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        recyclerView = view.findViewById(R.id.rcv_item_address);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1); // Thay đổi số cột tùy ý
        recyclerView.setLayoutManager(layoutManager);
        back = view.findViewById(R.id.back_address_button);
        addAddress = view.findViewById(R.id.add_item_address);
        addressItemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            cartItemArrayList = bundle.getParcelableArrayList("cartItemList");
            listIds = bundle.getIntegerArrayList("listId");
            method =bundle.getString("method",null);
            from = bundle.getString("from");
            id = bundle.getInt("id",0);
        }

        adapter = new AddressAdapter(addressItemList, requireActivity().getSupportFragmentManager(),id,cartItemArrayList,listIds,method);
        recyclerView.setAdapter(adapter);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToAddItemAddressFragment();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fetchAddressData();

        return view;
    }

    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
if(id==0) {
    IndividualFragment invIndividualFragment = new IndividualFragment();

    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.hide(this);  // Hide the current fragment instead of replacing it
    transaction.replace(R.id.fragment_container, invIndividualFragment);
    transaction.addToBackStack("address");
    transaction.commit();
}
else {
    PayFragment payFragment = new PayFragment();
    Bundle args = new Bundle();
    args.putParcelableArrayList("cartItemList", cartItemArrayList);
    args.putString("method",method);
    Log.d("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMEEETHODDDD","2");
    args.putIntegerArrayList("listId", listIds);
    args.putString("from",from);
    payFragment.setArguments(args);
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.fragment_container, payFragment);
    transaction.commit();
}
    }



    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }

    private void navigateToAddItemAddressFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        AddAddressFragment addAddressFragment = new AddAddressFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putParcelableArrayList("cartItemList", cartItemArrayList);
        args.putIntegerArrayList("listId", listIds);
        addAddressFragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(this);
        transaction.replace(R.id.fragment_container, addAddressFragment);
        transaction.addToBackStack("address");
        transaction.commit();
    }




    private void fetchAddressData() {
        ApiService.productServiceWithToken.getAddress(userId).enqueue(new Callback<List<AddressItem>>() {
            @Override
            public void onResponse(Call<List<AddressItem>> call, Response<List<AddressItem>> response) {
                if (response.isSuccessful()) {
                    List<AddressItem> newAddress = response.body();
                    if (newAddress != null && !newAddress.isEmpty()) {

                        Log.d("RCVADD", "onResponse:hoang ");
                        addressItemList.clear();
                        addressItemList.addAll(newAddress);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API Error", "Response is not successful");
                }

            }

            @Override
            public void onFailure(Call<List<AddressItem>> call, Throwable t) {
                String errorMessage = "API Call Failed: " + t.getMessage();
                Log.e("API Error", errorMessage);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("AddressFragment", "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
