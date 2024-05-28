package com.androidexam.fashionshop.Fragment.Pay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Adapter.PaymentAdapter;
import com.androidexam.fashionshop.Adapter.VoucherAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Decoration.MyInterface;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Order;
import com.androidexam.fashionshop.Model.ResponeVoucher;
import com.androidexam.fashionshop.Model.Responeurl;
import com.androidexam.fashionshop.Model.ResponseOrder;
import com.androidexam.fashionshop.Model.ShippingResponse;
import com.androidexam.fashionshop.Model.Voucher;
import com.androidexam.fashionshop.R;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayContentFragment extends Fragment {
    private RecyclerView rcvListBuy;
    private PaymentAdapter paymentAdapter;
    private List<Voucher> vouchers = new ArrayList<>();
    private double h=0,feeship=0,gs=0,gv=0;
    private int userId,idFreeShip=-1,idPurchase=-1;
    private RelativeLayout chooseAdress, voucherShip, voucher, chosenMethod;
    private ArrayList<CartItem> listBuy;
    private AddressItem addressItemChosen;
    private  List<Voucher>listVoucher=new ArrayList<>();
    private  List<Voucher>listVoucherChosen=new ArrayList<>();
    private TextView totalPay,totalPrice, totalPrice1, tvmethodPayment, tvTotalItem,namePeople,phonePeople,addressPeople ,streetPeople,feeShip,totalVoucherDiscount,totalFreeShip,codesShip,codeVoucher;
    private double s;

    private long districtId;
    private String wardCode;
    private MyInterface.OnFunctionXListener onFunctionXListener;


    private String actualMethod;
    private String method;
    public PayContentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_content, container, false);
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        rcvListBuy = view.findViewById(R.id.rcv_item_pay);
        chooseAdress = view.findViewById(R.id.chosen_address);
        totalPrice = view.findViewById(R.id.tv_total_price);
        totalPrice1 = view.findViewById(R.id.tv_total_price_item);
        voucherShip = view.findViewById(R.id.rtl_voucher_ship);
        voucher = view.findViewById(R.id.rtl_voucher);
        tvTotalItem = view.findViewById(R.id.tv_total_item);
        tvmethodPayment = view.findViewById(R.id.text_payment_method);
        chosenMethod = view.findViewById(R.id.rtl_chosen_method);
        namePeople = view.findViewById(R.id.name_people);
        phonePeople = view.findViewById(R.id.phone);
        addressPeople = view.findViewById(R.id.address);
        streetPeople = view.findViewById(R.id.street);
        codesShip = view.findViewById(R.id.txtship);
        codeVoucher = view.findViewById(R.id.txtvoucher);
        feeShip = view.findViewById(R.id.tv_total_price_ship);
        totalPay = view.findViewById(R.id.tv_total);
        totalFreeShip = view.findViewById(R.id.tv_total_discount_ship);
        totalVoucherDiscount = view.findViewById(R.id.tv_total_voucher_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rcvListBuy.setLayoutManager(layoutManager);
        // rcvListBuy.setAdapter(paymentAdapter);
        Bundle bundle = getArguments();

        if (listBuy != null) {
            listBuy.clear();
        }
        if (bundle != null) {
            listBuy = bundle.getParcelableArrayList("listBuyItem");
             method = bundle.getString("method");
            addressItemChosen = (AddressItem) bundle.getSerializable("addressChoose");

        }
        if (method!=null){

            tvmethodPayment.setText(method);
            PaymentMethodHelper.paymentMethodMap.get(method);

        }



        if (listBuy != null) {
            paymentAdapter = new PaymentAdapter(getContext(), listBuy);
            rcvListBuy.setAdapter(paymentAdapter);
            tvTotalItem.setText(String.valueOf(listBuy.size()));
        }
         s = 0;
        for (CartItem i : listBuy) {
            s += i.getDisPrice() * i.getQuantity();

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        h=s;
        totalPrice.setText("đ"+String.valueOf(decimalFormat.format(s)));
        totalPrice1.setText("đ"+String.valueOf(decimalFormat.format(s)));
        fetchAddressData(view);
        chooseAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddress();
//                AddressFragment addressFragment = new AddressFragment();
//                Bundle args = new Bundle();
//                args.putInt("id", 1);
//                addressFragment.setArguments(args);
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, addressFragment);
//                fragmentTransaction.addToBackStack("payment");
//                fragmentTransaction.commit();
            }

        });
        chosenMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChoseMethodPayment();
            }
        });


        voucherShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVoucher("FREE_SHIP");

            }
        });
        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVoucher("PURCHASE");

            }
        });

        List<Integer>listVoucherId=new ArrayList<>();
        if(idPurchase!=-1) {
            listVoucherId.add(idPurchase);
        }
        if (idFreeShip!=-1){
            listVoucherId.add(idFreeShip);
        }

        return view;
    }

    private void chooseVoucher(String value) {

        ApiService.productServiceWithToken.getVoucher(value).enqueue(new Callback<ResponeVoucher>() {
            @Override
            public void onResponse(Call<ResponeVoucher> call, Response<ResponeVoucher> response) {

                ResponeVoucher a = response.body();

                if (a.getContent().size() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_voucher, null);
                    RecyclerView recyclerView = dialogView.findViewById(R.id.rcv_voucher);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(layoutManager);
                    listVoucher= a.getContent();
                    VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), a.getContent(), value);
                    recyclerView.setAdapter(voucherAdapter);

                    builder.setView(dialogView)
                            .setTitle("Chọn Voucher")
                            .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int selectedVoucherId = voucherAdapter.getSelectedVoucherId();
                                    Voucher voucher1 = voucherAdapter.getSelectedVoucher();
                                    if (value.equals("FREE_SHIP")) {
                                        idFreeShip = selectedVoucherId;
                                        double a=0;

                                        a += getValueDiscount(voucher1,s);


                                        gs=a;

                                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                        codesShip.setText(voucherAdapter.getCode());
                                        totalFreeShip.setText("đ"+String.valueOf(decimalFormat.format(a)));
                                        totalPay.setText("đ"+decimalFormat.format((int) (h+feeship-gs-gv)));
                                    } else if (value.equals("PURCHASE")) {
                                        idPurchase = selectedVoucherId;
                                        double a=0;

                                                a += getValueDiscount(voucher1,s);


                                                gv=a;
                                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

                                        totalVoucherDiscount.setText(String.valueOf("đ"+decimalFormat.format(a)));
                                        totalPay.setText("đ"+decimalFormat.format((int) (h+feeship-gs-gv)));
                                        codeVoucher.setText(voucherAdapter.getCode());
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


            } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_voucher, null);
                    builder.setView(dialogView).setTitle("Chọn Voucher")
                            .setMessage("Không có voucher khả dụng.")
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            }

            @Override
            public void onFailure(Call<ResponeVoucher> call, Throwable t) {
                Log.e("API Call", "Failed", t);

            }
        });


    }

    private void ShowChoseMethodPayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.sizedialog, null);
        builder.setView(dialogView).setTitle("Chọn phương thức thanh toán");
        AlertDialog alertDialog = builder.create();
        ListView listView = dialogView.findViewById(R.id.listViewSize);
        List<String> methodList = Arrays.asList("Thanh toán khi nhận hàng", "Ví VNPay");

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, methodList);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMethod = methodList.get(position);
                tvmethodPayment.setText(selectedMethod);
                actualMethod = PaymentMethodHelper.paymentMethodMap.get(selectedMethod);
                alertDialog.dismiss();

            }
        };
        listView.setOnItemClickListener(itemClickListener);
        listView.setAdapter(sizeAdapter);
        alertDialog.show();
    }


    private void fetchAddressData(View view) {

        if (addressItemChosen == null) {
            ApiService.productServiceWithToken.getAddress(userId).enqueue(new Callback<List<AddressItem>>() {
                @Override
                public void onResponse(Call<List<AddressItem>> call, Response<List<AddressItem>> response) {
                    if (response.isSuccessful()) {
                        List<AddressItem> ListAddress = response.body();
                        if (ListAddress != null && !ListAddress.isEmpty()) {

                            for (AddressItem addressItem : ListAddress) {
                                if (addressItem.isDefault()) {
                                    namePeople.setText(addressItem.getName());
                                    addressPeople.setText(addressItem.getAddress());
                                    streetPeople.setText(addressItem.getStreet());
                                    phonePeople.setText(addressItem.getPhoneNumber());
                                    districtId = addressItem.getDistrictId();
                                    wardCode = addressItem.getWardCode();

                                }
                            }
                            ApiService.productServiceWithToken.getFeeShip(districtId,wardCode).enqueue(new Callback<ShippingResponse>() {
                                @Override
                                public void onResponse(Call<ShippingResponse> call, Response<ShippingResponse> response) {
                                    if(response.isSuccessful()){
                                        int FeeShip= response.body().getData().getFeeShip();
                                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                                        feeship=FeeShip;
                                        feeShip.setText("đ"+decimalFormat.format(FeeShip));
                                        totalPay.setText("đ"+decimalFormat.format((int) (h+feeship-gs-gv)));
                                    }


                                }

                                @Override
                                public void onFailure(Call<ShippingResponse> call, Throwable t) {

                                }
                            });
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
            Log.d("ADD",wardCode+String.valueOf(districtId));

        } else {
            namePeople.setText(addressItemChosen.getName());
            addressPeople.setText(addressItemChosen.getAddress());
            streetPeople.setText(addressItemChosen.getStreet());
            phonePeople.setText(addressItemChosen.getPhoneNumber());
            districtId = addressItemChosen.getDistrictId();
            wardCode = addressItemChosen.getWardCode();
            ApiService.productServiceWithToken.getFeeShip(districtId,wardCode).enqueue(new Callback<ShippingResponse>() {
                @Override
                public void onResponse(Call<ShippingResponse> call, Response<ShippingResponse> response) {
                    if(response.isSuccessful()){
                        int FeeShip= response.body().getData().getFeeShip();
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        feeship=FeeShip;
                        feeShip.setText("đ"+decimalFormat.format(FeeShip));
                        totalPay.setText("đ"+decimalFormat.format((int) (h+feeship-gs-gv)));
                    }


                }

                @Override
                public void onFailure(Call<ShippingResponse> call, Throwable t) {

                }
            });
        }



    }

    private void openAddress() {
        if (onFunctionXListener != null) {
            onFunctionXListener.onFunctionX();
        }
    }

    public void setOnFunctionXListener(MyInterface.OnFunctionXListener listener) {
        this.onFunctionXListener = listener;
    }

    public void Pay(){
        if(tvmethodPayment.getText() != null && !tvmethodPayment.getText().toString().isEmpty())  {
            Order order = new Order();
            order.setName(namePeople.getText().toString());
            order.setPhoneNumber(phonePeople.getText().toString());
            order.setShippingAddress(addressPeople.getText().toString() + "," + streetPeople.getText().toString());
            order.setPaymentMethod(actualMethod);

            List<CartItem> listOrder = new ArrayList<>();
            for (CartItem i : listBuy
            ) {
                listOrder.add(new CartItem(i.getId(), i.getQuantity(), i.getProductId(), i.getSize()));

            }
            order.setOrderItems(listOrder);

            order.setUserId(userId);
            order.setWardCode(wardCode);
            order.setDistrictId(districtId);
            Log.d("ADD", wardCode + String.valueOf(districtId));
            List<Integer> listIdVoucher = new ArrayList<>();
            if (idPurchase != -1) {
                listIdVoucher.add(idPurchase);
            }
            if (idFreeShip != -1) {
                listIdVoucher.add(idFreeShip);
            }
            order.setIdsVoucher(listIdVoucher);

            order.setFeeShip(feeship);
            order.setDiscountAmount(gv);
            order.setDiscountShippingFee(gs);

            ApiService.productServiceWithToken.postOrder(order).enqueue(new Callback<Responeurl>() {
                @Override
                public void onResponse(Call<Responeurl> call, Response<Responeurl> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Đặt hàng thành công.", Toast.LENGTH_SHORT).show();
                        Responeurl responseOrder = response.body();
                        if (responseOrder.getUrlPayment() != null) {
                            String url = responseOrder.getUrlPayment();


                            VNPay(url);
                        }
                        else {
                         onHome();}

                    } else {
                        Log.e("API Error", response.errorBody().toString());
                        Log.e("API Error", "Response is not successful");
                    }

                }

                @Override
                public void onFailure(Call<Responeurl> call, Throwable t) {
                    Toast.makeText(getContext(), "Đặt hàng không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();

                }
            });

        }
        else{
            Toast.makeText(getContext(), "Vui lòng chọn phương thức thanh toán.", Toast.LENGTH_SHORT).show();
        }
    }

    private void VNPay(String url){
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        webViewFragment.setArguments(args);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, webViewFragment);
        transaction.commit();
    }
    public long getValueDiscount(Voucher voucher, double valueOrder) {
        if (isVoucherApplicable(valueOrder, voucher)) {
            double discountValue = 0;
            switch (voucher.getDiscountType()) {
                case "PERCENTAGE":
                    discountValue = (valueOrder * voucher.getDiscountValue() / 100);
                    discountValue = Math.min(discountValue, voucher.getMaxDiscountValue());
                    break;
                case "AMOUNT":
                    discountValue = voucher.getDiscountValue();
                    break;
            }
            return (long) discountValue;
        }
        return 0;
    }
    private boolean isVoucherApplicable(double valueOrder, Voucher voucher) {
        if (voucher == null) {
            Log.e("VoucherManager", "Voucher is null");
        }
        if (!voucher.isActive()) {
            Log.e("VoucherManager", "Voucher is not active");
        }
        if (voucher.getExpiryDate().isBefore(LocalDateTime.now())) {
            Log.e("VoucherManager", "Voucher has expired");
        }
        if (valueOrder < voucher.getMinimumPurchaseAmount()) {
            Log.e("VoucherManager", "Order value does not meet the minimum requirement");
        }
        if (voucher.getUsageCount() >= voucher.getUsageLimit()) {
            Log.e("VoucherManager", "Voucher usage limit has been reached");
        }
        return true;
    }
    private void onHome() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }
    public String getMethod(){
        return tvmethodPayment.getText().toString();
    }

}

