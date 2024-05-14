package com.androidexam.fashionshop.Fragment.Individual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Pay.PayFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.AddressViewModel;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressFragment extends Fragment {
    private AddressViewModel addressViewModel;
    private ImageButton back;
    private RelativeLayout chooseAddress;
    private int userId;
    private Switch mySwitch;
    private boolean isSwitchChecked = false;
    private TextView addAddress,phoneNumberTextView,streetTextView,nameTextView,nameAddressTextView,delete;
    private int addressId;
    private  String WardCode ;
    private ArrayList<CartItem> cartItemArrayList ;
    private ArrayList<Integer> listIds;
    private int id=0;
    private long DistrictID ;
    private AddressItem item;

    public AddAddressFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressViewModel = new ViewModelProvider(requireActivity()).get(AddressViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_address, container, false);
        hideBottomNavigationView();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        Log.e("User", "userid: " +userId);
        addAddress = view.findViewById(R.id.btn_add_address);
        back = view.findViewById(R.id.back_address_button);
        mySwitch = view.findViewById(R.id.Switch);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isSwitchChecked = isChecked;
            }
        });
        chooseAddress = view.findViewById(R.id.choose_address);
        nameTextView = view.findViewById(R.id.edt_name_address);
        phoneNumberTextView = view.findViewById(R.id.edt_phone_number_address);
        streetTextView = view.findViewById(R.id.edt_name_street_address);
        nameTextView.setText(addressViewModel.getName());
        phoneNumberTextView.setText(addressViewModel.getPhoneNumber());
        streetTextView.setText(addressViewModel.getStreet());
        nameAddressTextView = view.findViewById(R.id.name_address);
        delete = view.findViewById(R.id.btn_delete_address);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cartItemArrayList = bundle.getParcelableArrayList("cartItemList");
            listIds = bundle.getIntegerArrayList("listId");
            id = bundle.getInt("id",0);
            WardCode= bundle.getString("WardCode");
            DistrictID = bundle.getInt("DistrictID",-1);


             item =(AddressItem) bundle.getSerializable("addressItem");
            String valueAddress = bundle.getString("selectedAddress", null);


            if (item != null  ) {
                delete.setVisibility(View.VISIBLE);
                getAddressItem(item);
                if(valueAddress==null){
                WardCode= item.getWardCode();
                DistrictID = item.getDistrictId();}
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteAddress(item.getId());
                    }
                });
                addAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditAddress(item.getId());
                    }
                });
                chooseAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openeditChooseAddress();
                    }
                });

            }  else {
                delete.setVisibility(View.GONE);
                addAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddAddress();
                    }
                });
                chooseAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openChooseAddress();
                    }
                });
            }
            if (valueAddress != null) {
                nameAddressTextView.setText(valueAddress);
            }
            }else {
            delete.setVisibility(View.GONE);
            addAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAddress();
                }
            });
            chooseAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openChooseAddress();
                }
            });
        }




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return view;
    }
    public boolean getSwitchValue() {
        return isSwitchChecked;
    }

    private void DeleteAddress(int id) {

        ApiService.productServiceWithToken.deleteadd(userId,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                        Log.e("Delete", "thanh cong " );
                        onBackPressed();
                } else {
                    Log.e("User", "Error: " + response.message());
                    Log.e("User", "Error Code: " + response.code());
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }

    private void getAddressItem(AddressItem item) {
        nameTextView.setText(item.getName());
        streetTextView.setText(item.getStreet());
        mySwitch.setChecked(item.isDefault());
        phoneNumberTextView.setText(item.getPhoneNumber());
        nameAddressTextView.setText(item.getAddress());

    }

    private void AddAddress() {
        String phoneNumber = phoneNumberTextView.getText().toString();
        String nameAddress = nameAddressTextView.getText().toString();
        String name = nameTextView.getText().toString();
        String street = streetTextView.getText().toString();

        // Kiểm tra xem tất cả các TextView đã được điền đầy đủ thông tin hay không
        if (!phoneNumber.isEmpty() && !nameAddress.isEmpty() && !name.isEmpty() && !street.isEmpty()) {
            // Kiểm tra định dạng số điện thoại
            if (isValidPhoneNumber(phoneNumber)) {
                // Tạo đối tượng AddressItem và thiết lập các giá trị
                AddressItem addressItem = new AddressItem();
                addressItem.setPhoneNumber(phoneNumber);
                addressItem.setAddress(nameAddress);
                addressItem.setName(name);
                addressItem.setStreet(street);
                addressItem.setDefault(getSwitchValue());
                addressItem.setDistrictId(DistrictID);
                addressItem.setWardCode(WardCode);

                // Gọi API để thêm địa chỉ
                ApiService.productServiceWithToken.addAddress(userId, addressItem).enqueue(new Callback<AddressItem>() {
                    @Override
                    public void onResponse(Call<AddressItem> call, Response<AddressItem> response) {
                        if (response.isSuccessful()) {
                            AddressItem addressItem = response.body();
                            if (addressItem != null) {
                                // Xử lý khi thêm địa chỉ thành công
                                onBackPressed();
                            }
                        } else {
                            Log.e("User", "Error: " + response.errorBody());
                            Log.e("User", "Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<AddressItem> call, Throwable t) {
                        Log.e("User", "API Call Failed: " + t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getContext(), "Số điện thoại không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
        }
    }


    private void EditAddress(int addressId) {
        AddressItem addressItem =new AddressItem();
        addressItem.setPhoneNumber(phoneNumberTextView.getText().toString());
        addressItem.setAddress(nameAddressTextView.getText().toString());
        addressItem.setName(nameTextView.getText().toString());
        addressItem.setStreet(streetTextView.getText().toString());
        addressItem.setDefault(getSwitchValue());
        addressItem.setDistrictId(DistrictID);
        addressItem.setWardCode(WardCode);

        ApiService.productServiceWithToken.editAddress(userId,addressId, addressItem).enqueue(new Callback<AddressItem>() {
            @Override
            public void onResponse(Call<AddressItem> call, Response<AddressItem> response) {
                if (response.isSuccessful()) {
                    AddressItem addressItem = response.body();
                    if (addressItem != null) {
                        Log.e("Edit", "thanh cong " );
                        onBackPressed();
                    }
                } else {
                    try {
                        // Lấy thông điệp lỗi từ phản hồi
                        String errorMessage = response.errorBody().string();
                        // Nếu thông điệp lỗi là JSON, bạn có thể sử dụng một thư viện JSON để phân tích cú pháp
                        JSONObject errorJson = new JSONObject(errorMessage);
                        String message = errorJson.getString("message");

                        // Hiển thị thông báo lỗi hoặc xử lý theo nhu cầu của bạn
                        Log.e("LoginFragment", "Login failed: " + message);
                        // Bạn có thể hiển thị thông báo lỗi cho người dùng
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("User", "Error: " + response.errorBody());
                    Log.e("User", "Error Code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<AddressItem> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }

    private void openChooseAddress() {
        TextView nameTv = getView().findViewById(R.id.edt_name_address);
        TextView phoneNumberTv = getView().findViewById(R.id.edt_phone_number_address);
        TextView streetTv = getView().findViewById(R.id.edt_name_street_address);
        addressViewModel.setName(nameTv.getText().toString());
        addressViewModel.setPhoneNumber(phoneNumberTv.getText().toString());
        addressViewModel.setStreet(streetTv.getText().toString());

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ChooseAddressFragment chooseAddressFragment = new ChooseAddressFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putParcelableArrayList("cartItemList", cartItemArrayList);
        args.putIntegerArrayList("listId", listIds);
        chooseAddressFragment.setArguments(args);
        transaction.replace(R.id.fragment_container, chooseAddressFragment);
        transaction.addToBackStack("Add");
        transaction.commit();
    }
    private void openeditChooseAddress() {
        TextView nameTv = getView().findViewById(R.id.edt_name_address);
        TextView phoneNumberTv = getView().findViewById(R.id.edt_phone_number_address);
        TextView streetTv = getView().findViewById(R.id.edt_name_street_address);
        addressViewModel.setName(nameTv.getText().toString());
        addressViewModel.setPhoneNumber(phoneNumberTv.getText().toString());
        addressViewModel.setStreet(streetTv.getText().toString());

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ChooseAddressFragment chooseAddressFragment = new ChooseAddressFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();

        args.putInt("id", id);
        args.putSerializable("addressItem", item);
        args.putParcelableArrayList("cartItemList", cartItemArrayList);
        args.putIntegerArrayList("listId", listIds);
        chooseAddressFragment.setArguments(args);
        transaction.replace(R.id.fragment_container, chooseAddressFragment);
        transaction.addToBackStack("Add");
        transaction.commit();
    }
    private void onBackPressed() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();


        if(id==0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new AddressFragment());
            transaction.commit();
        }
        else {
            AddressFragment addressFragment = new AddressFragment();
            Bundle args = new Bundle();
            args.putInt("id", 1);
            args.putParcelableArrayList("cartItemList", cartItemArrayList);
            args.putIntegerArrayList("listId", listIds);
            addressFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, addressFragment);
            transaction.commit();
        }

    }

    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }
    private void addaddressdone() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new AddressFragment());
        transaction.commit();

    }
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy cho số điện thoại Việt Nam
        String regex = "((09|03|07|08|05)+([0-9]{8})\\b)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        // Kiểm tra xem chuỗi có khớp với biểu thức chính quy hay không
        return matcher.matches();
    }

}
