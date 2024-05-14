package com.androidexam.fashionshop.Fragment.Individual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidexam.fashionshop.Api.ApiAddress;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.ApiResponse;
import com.androidexam.fashionshop.Model.ApiResponseDistrict;
import com.androidexam.fashionshop.Model.ApiResponseWard;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.District;
import com.androidexam.fashionshop.Model.Province;
import com.androidexam.fashionshop.Model.Ward;
import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAddressFragment extends Fragment {

    private Spinner spinnerProvinces, spinnerDistricts, spinnerWards;
    private List<Province> provinceList;
    private List<District> districtList;
    private ImageButton back;
    private List<Ward> wardList;
    private int id=0;
    private boolean userInteracted = false;
    private boolean userInteracted2 = false;
    private boolean userInteracted3 = false;
    private ArrayList<CartItem> cartItemArrayList ;
    private ArrayList<Integer> listIds;
    private AddressItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choosen_address, container, false);


        spinnerProvinces = view.findViewById(R.id.spinnerProvinces);
        spinnerDistricts = view.findViewById(R.id.spinnerDistricts);
        spinnerWards = view.findViewById(R.id.spinnerWards);
        back = view.findViewById(R.id.back_address);
        spinnerWards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userInteracted3 = true;
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            cartItemArrayList = bundle.getParcelableArrayList("cartItemList");
            listIds = bundle.getIntegerArrayList("listId");
            item =(AddressItem) bundle.getSerializable("addressItem");
            id = bundle.getInt("id",0);}

        ApiAddress apiAddress = ApiAddress.retrofit.create(ApiAddress.class);
        Call<ApiResponse> callProvinces = apiAddress.getProvinces("c61b8d62-a18d-11ee-a6e6-e60958111f48");
        callProvinces.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    provinceList = response.body().getData();


                    List<String> provinceNames = new ArrayList<>();
                    for (Province province : provinceList) {
                        provinceNames.add(province.getProvinceName());
                    }


                    provinceNames.add(0, "Chọn tỉnh/thành phố");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, provinceNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvinces.setAdapter(adapter);
                    spinnerProvinces.setSelection(0);


                    spinnerProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (userInteracted && position > 0) {
                                    int selectedProvinceId = provinceList.get(position-1).getProvinceID();
                                    loadDistricts(selectedProvinceId);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    spinnerProvinces.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            userInteracted = true;
                            return false;
                        }
                    });
                } else {
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });



        return view;
    }

    private void loadDistricts(int provinceId) {
        ApiAddress apiAddress = ApiAddress.retrofit.create(ApiAddress.class);
        Call<ApiResponseDistrict> callDistricts = apiAddress.getDistricts("c61b8d62-a18d-11ee-a6e6-e60958111f48",provinceId);
        callDistricts.enqueue(new Callback<ApiResponseDistrict>() {
            @Override
            public void onResponse(Call<ApiResponseDistrict> call, Response<ApiResponseDistrict> response) {
                if (response.isSuccessful()) {
                    districtList = new ArrayList<>();
                    districtList= response.body().getData();

                    List<String> districtNames = new ArrayList<>();
                    for (District district : districtList) {
                        districtNames.add(district.getDistrictName());
                    }

                    districtNames.add(0, "Chọn quận/huyện");
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtNames);
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDistricts.setAdapter(districtAdapter);
                    spinnerDistricts.setSelection(0);

                    spinnerDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (userInteracted2 && position > 0) {
                                int selectedDistrictId = districtList.get(position - 1).getDistrictID(); // Giảm đi 1 ở đây
                                loadWards(selectedDistrictId);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });


                    spinnerDistricts.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, android.view.MotionEvent event) {
                            userInteracted2 = true;
                            return false;
                        }
                    });
                } else {
                }

            }

            @Override
            public void onFailure(Call<ApiResponseDistrict> call, Throwable t) {

            }
        });
    }


    private void loadWards(int districtId) {
        ApiAddress apiAddress = ApiAddress.retrofit.create(ApiAddress.class);
        Call<ApiResponseWard> callWards = apiAddress.getWards("c61b8d62-a18d-11ee-a6e6-e60958111f48",districtId);
        callWards.enqueue(new Callback<ApiResponseWard>() {
            @Override
            public void onResponse(Call<ApiResponseWard> call, Response<ApiResponseWard> response) {
                if (response.isSuccessful()) {
                    wardList = new ArrayList<>();
                    wardList=  response.body().getData();
                    List<String> wardNames = new ArrayList<>();
                    for (Ward ward : wardList) {
                        wardNames.add(ward.getWardName());
                    }

                    wardNames.add(0, "Chọn xã/phường");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, wardNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerWards.setAdapter(adapter);
                    spinnerWards.setSelection(0);
                    spinnerWards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (userInteracted3) {
                                String WardCode = wardList.get(position - 1).getWardCode(); // Giảm đi 1 ở đây
                                int DistrictID = wardList.get(position -1).getDistrictID();
                                navigateBack(WardCode,DistrictID);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
                } else {

                }

            }

            @Override
            public void onFailure(Call<ApiResponseWard> call, Throwable t) {

            }
        });
    }

    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private String buildSelectedAddress() {
        String selectedProvince = spinnerProvinces.getSelectedItem().toString();
        String selectedDistrict = spinnerDistricts.getSelectedItem().toString();
        String selectedWard = spinnerWards.getSelectedItem().toString();

        return selectedProvince + ", " + selectedDistrict + ", " + selectedWard;
    }

    private void navigateBack(String WardCode, int DistrictID) {
        AddAddressFragment addAddressFragment = new AddAddressFragment();
        Bundle args = new Bundle();
        String selectedAddress = buildSelectedAddress();
        args.putString("selectedAddress", selectedAddress);
        args.putString("WardCode", WardCode);
        if(item!=null){
            args.putSerializable("addressItem", item);
        }
        args.putInt("DistrictID", DistrictID);
        args.putInt("id", id);
        args.putParcelableArrayList("cartItemList", cartItemArrayList);
        args.putIntegerArrayList("listId", listIds);
        addAddressFragment.setArguments(args);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, addAddressFragment);
        transaction.commit();
    }
}
