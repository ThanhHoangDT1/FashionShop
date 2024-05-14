package com.androidexam.fashionshop.Fragment.ResetPassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.ResetPassword.EnterOTPFragment;
import com.androidexam.fashionshop.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassWordFragment extends Fragment {


    private EditText usernameEditText;
    private TextView btnContinue;
    private ImageButton back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forgot_password, container, false);

        usernameEditText = view.findViewById(R.id.edt_us);
        btnContinue =  view.findViewById(R.id.btn_continue);
        back = view.findViewById(R.id.back_forgotpw_button);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOTP();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        return view;
    }

    private void SendOTP() {
        ApiService.productService.sendUsername(usernameEditText.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String email = response.body().string();

                        navigateToEnterOTPFragment(email);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("User", "Error reading response body: " + e.getMessage());
                    }
                } else {
                    Log.e("User", "Error: " + response.message());
                    Log.e("User", "Error Code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }
    private void navigateToEnterOTPFragment(String email) {



        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        EnterOTPFragment enterOTPFragment= new EnterOTPFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("username", usernameEditText.getText().toString());
        enterOTPFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, enterOTPFragment)
                .addToBackStack(null)
                .commit();

    }
    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

}
