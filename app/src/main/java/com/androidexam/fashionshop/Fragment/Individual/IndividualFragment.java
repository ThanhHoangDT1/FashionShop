package com.androidexam.fashionshop.Fragment.Individual;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidexam.fashionshop.Adapter.OrderProductItemAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.HistoryBuy.DeliveredFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.HistoryFragment;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.OrderItem;
import com.androidexam.fashionshop.Model.ResponseGetOrderItemDelivered;
import com.androidexam.fashionshop.Model.User;
import com.androidexam.fashionshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualFragment extends Fragment {

    private User user;
    private int userId;
    private String accessToken;
    private ImageView avatar;
    private TextView username, name,quatityRate;
    private LinearLayout rate,inTransit;
    private HistoryFragment historyFragment;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private RelativeLayout account, logout, address,bill;

    public IndividualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        ShowBottomNavigationView();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getContext(),gso);

        if(userId!=-1){


            getquatityorder();
            avatar = view.findViewById(R.id.yourAvatar);
            username = view.findViewById(R.id.tvUser);
            name = view.findViewById(R.id.tvName);
            account = view.findViewById(R.id.tvaccount);
            address = view.findViewById(R.id.address);
            bill = view.findViewById(R.id.bill);
            quatityRate = view.findViewById(R.id.quatity_rate);
            logout = view.findViewById(R.id.logout);
            rate =view.findViewById(R.id.lnl_rate);
            inTransit = view.findViewById(R.id.lnl_in_transit);
            inTransit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HistoryFragment historyFragment = new HistoryFragment();

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, historyFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            historyFragment.goToTab(1);
                            requireActivity().getSupportFragmentManager().removeOnBackStackChangedListener(this);
                        }
                    });
                }
            });

            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Onrate();
                }
            });
            Log.d("Iduser", String.valueOf(userId));
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();

                }
            });
            bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openHistoryOrder();
                }
            });
            account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccount();

                }
            });

            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAddress();

                }
            });


            getUserDetail(userId);
        }
        else {
            navigateToLoginFragment();
        }
        return view;

    }

    private void Onrate() {
        DeliveredFragment deliveredFragment = new DeliveredFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, deliveredFragment);
        fragmentTransaction.addToBackStack("Individual");
        fragmentTransaction.commit();

    }

    private void openHistoryOrder() {


        HistoryFragment purchaseOrderFragment = new HistoryFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, purchaseOrderFragment);
        fragmentTransaction.addToBackStack("Individual");
        fragmentTransaction.commit();
    }

    private void openAddress() {

        AddressFragment addressFragment = new AddressFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, addressFragment);
        fragmentTransaction.addToBackStack("Individual");
        fragmentTransaction.commit();
    }

    private void openAccount() {
        AccountFragment accountFragment = new AccountFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, accountFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void getUserDetail(int userId) {
        ApiService.productServiceWithToken.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        setUser(user);
                    }
                } else {
                    Log.e("User", "Error: " + response.message());
                    Log.e("User", "Error Code: " + response.code());
                    navigateToLoginFragment();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        displayUserDetails();
    }

    private void displayUserDetails() {

        if (user.getUrlImage() == null) {
            String name = user.getName();
            String firstLetter = name.substring(0, 1).toLowerCase();

            // Thay đổi ảnh của ImageView thành ảnh chữ cái
            int imageResource = getContext().getResources().getIdentifier(
                    firstLetter,
                    "drawable",
                    getContext().getPackageName()

            );
            Log.d("AvatarImage", "Image Resource: " + imageResource);
            avatar.setImageResource(imageResource);
        } else {
            Picasso.get().load(user.getUrlImage()).into(avatar);
        }
        username.setText(user.getUsername());
        name.setText(user.getName());


    }


    private void navigateToLoginFragment() {


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commit();
    }
    public void logout() {
        SharedPreferences preferencesID = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        preferencesID.edit().clear().apply();

        SharedPreferences preferencesAC = requireContext().getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        preferencesAC.edit().clear().apply();

        SharedPreferences preferencesRF = requireContext().getSharedPreferences("REFRESH", Context.MODE_PRIVATE);
        preferencesRF.edit().clear().apply();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if(acct!=null){
            signOut();

        }
        else{



        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commit();}
    }
    private void signOut() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        LoginFragment loginFragment = new LoginFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, loginFragment)
                                .commit();
                    }
                });
    }


    private void ShowBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }
    public void getquatityorder(){
        ApiService.productServiceWithToken.getOrderItemDelivered(userId,false).enqueue(new Callback<ResponseGetOrderItemDelivered>() {
            @Override
            public void onResponse(Call<ResponseGetOrderItemDelivered> call, Response<ResponseGetOrderItemDelivered> response) {
                if(response.isSuccessful()){

                    quatityRate.setText( String.valueOf(response.body().getNumberOfElement()));
                }
            }

            @Override
            public void onFailure(Call<ResponseGetOrderItemDelivered> call, Throwable t) {

            }
        });
    }


}