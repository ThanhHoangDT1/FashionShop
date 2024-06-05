package com.androidexam.fashionshop.Fragment.HistoryBuy;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Adapter.PaymentAdapter;
import com.androidexam.fashionshop.Adapter.VoucherAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Decoration.MyInterface;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Fragment.Pay.PaymentMethodHelper;
import com.androidexam.fashionshop.Fragment.Pay.WebViewFragment;
import com.androidexam.fashionshop.Model.AddressItem;
import com.androidexam.fashionshop.Model.CartItem;
import com.androidexam.fashionshop.Model.Order;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.Model.ResponeVoucher;
import com.androidexam.fashionshop.Model.ResponseOrder;
import com.androidexam.fashionshop.Model.ShippingResponse;
import com.androidexam.fashionshop.Model.Voucher;
import com.androidexam.fashionshop.R;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderedDetailContentFragment extends Fragment {
    private String orderedId;
    private ResponseOrder orderDetail;
    private RecyclerView rcvListBuy;
    private PaymentAdapter paymentAdapter;
    private LinearLayout cancel;
    private List<Voucher> vouchers = new ArrayList<>();
    private ArrayList<CartItem> listBuy=new ArrayList<>();
    private AddressItem addressItemChosen;
    private List<Voucher> listVoucher = new ArrayList<>();
    private List<Voucher> listVoucherChosen = new ArrayList<>();
    private TextView totalPay, totalPrice, totalPrice1, tvmethodPayment, tvTotalItem, namePeople, phonePeople, addressPeople, feeShip, totalVoucherDiscount, totalFreeShip, time,status;
    private double s;
    private String url;


    private String actualMethod;

    public OrderedDetailContentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ordered_detail_content, container, false);
        rcvListBuy = view.findViewById(R.id.rcv_item_pay);

        totalPrice = view.findViewById(R.id.tv_total_price);
        totalPrice1 = view.findViewById(R.id.tv_total_price_item);

        tvTotalItem = view.findViewById(R.id.tv_total_item);
        tvmethodPayment = view.findViewById(R.id.text_payment_method);

        cancel = view.findViewById(R.id.lnl_cance);
        time =view.findViewById(R.id.text_time);
        status = view.findViewById(R.id.text_status);
        namePeople = view.findViewById(R.id.name_people);
        phonePeople = view.findViewById(R.id.phone);
        addressPeople = view.findViewById(R.id.address);
        feeShip = view.findViewById(R.id.tv_total_price_ship);
        totalPay = view.findViewById(R.id.tv_total);
        totalFreeShip = view.findViewById(R.id.tv_total_discount_ship);
        totalVoucherDiscount = view.findViewById(R.id.tv_total_voucher_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rcvListBuy.setLayoutManager(layoutManager);
        // rcvListBuy.setAdapter(paymentAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderedId = bundle.getString("orderedId","");
        }
        fetchAddressData();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder();
            }
        });


        return view;
    }

    private void CancelOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Xác Nhận Hủy Đơn Hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng không?")
                .setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiService.productServiceWithToken.cancelOrder(orderedId,"CANCELLED").enqueue(new Callback<ResponseOrder>() {
                            @Override
                            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                                if( response.isSuccessful()){
                                    Toast.makeText(getContext(), "Đã hủy đơn thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    fetchAddressData();
                                }
                                else {
                                    Toast.makeText(getContext(), "Hủy đơn không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseOrder> call, Throwable t) {

                                Toast.makeText(getContext(), "Hủy đơn không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });





                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi phương thức onConfirmationDialogNegativeClick trong interface
                        dialog.cancel();
                        }
                });

            AlertDialog alertDialog = builder.create();
                    alertDialog.show();


    }


    private void fetchAddressData() {

        if (orderedId != "") {
            ApiService.productServiceWithToken.getOrderDetail(orderedId).enqueue(new Callback<ResponseOrder>() {
                @Override
                public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                    if (response.isSuccessful()) {
                        orderDetail = response.body();
                        if (orderDetail != null) {
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            namePeople.setText(orderDetail.getName());
                            addressPeople.setText(orderDetail.getShippingAddress());
                            phonePeople.setText(orderDetail.getPhoneNumber());
                            feeShip.setText("đ"+decimalFormat.format(orderDetail.getShippingFee()));
                            tvmethodPayment.setText(PaymentMethodHelper.paymentMethodMap.get(orderDetail.getPaymentMethod()));
                            totalFreeShip.setText("đ"+decimalFormat.format(orderDetail.getDiscountShippingFee()));
                            totalVoucherDiscount.setText("đ"+decimalFormat.format(orderDetail.getDiscountAmount()));
                            totalPrice.setText("đ" + decimalFormat.format(orderDetail.getTotalProductAmount()));
                            totalPrice1.setText("đ" + decimalFormat.format(orderDetail.getTotalProductAmount()));
                            tvTotalItem.setText(String.valueOf(orderDetail.getOrderItems().size()));
                            time.setText(orderDetail.getOrderDate());
                            status.setText((PaymentMethodHelper.paymentMethodMap.get(orderDetail.getOrderStatus())));
                            if(orderDetail.getOrderStatus().equals("UNCONFIRMED") ||orderDetail.getOrderStatus().equals("CONFIRMED")||orderDetail.getOrderStatus().equals("PREPARING_PAYMENT")){
                                cancel.setVisibility(View.VISIBLE);
                            }
                            else{
                                cancel.setVisibility(View.GONE);
                            }
                            totalPay.setText("đ"+decimalFormat.format(orderDetail.getTotalPayment()));
                            listBuy.clear();
                            for (OrderItem orderItem: orderDetail.getOrderItems()
                                 ) {

                                listBuy.add(new CartItem(orderItem.getQuantity(),orderItem.getProductId(),orderItem.getSizeType(),(long)orderItem.getUnitPrice(),url));

                            }
                            paymentAdapter = new PaymentAdapter(getContext(), listBuy);
                            rcvListBuy.setAdapter(paymentAdapter);

                        }
                    } else {
                        Log.e("API Error", "Response is not successful");
                    }

                }

                @Override
                public void onFailure(Call<ResponseOrder> call, Throwable t) {
                    String errorMessage = "API Call Failed: " + t.getMessage();
                    Log.e("API Error", errorMessage);
                }
            });

        }


    }






}

