package com.androidexam.fashionshop.Fragment.ResetPassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.androidexam.fashionshop.Model.ResponseForgotPW;
import com.androidexam.fashionshop.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOTPFragment extends Fragment {

    private String email;

    private TextView tvEmail,btnContinue;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private String username;
    private ImageButton back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.enter_otp, container, false);


        back = view.findViewById(R.id.back_otp_button);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);
        tvEmail = view.findViewById(R.id.tv_email);
        btnContinue = view.findViewById(R.id.btn_confirm);
        editText1.requestFocus();
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("email", null);
            username = bundle.getString("username", null);
            if (email != null) {
                tvEmail.setText(email);
            } else {
            }
        }
        setEditTextListeners();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOTP();
            }
        });
        return view;
    }

    private void ConfirmOTP() {

        String otp = "";
        otp += editText1.getText().toString();
        otp += editText2.getText().toString();
        otp += editText3.getText().toString();
        otp += editText4.getText().toString();
        otp += editText5.getText().toString();
        otp += editText6.getText().toString();
        ApiService.productService.verifyOTP(username,otp).enqueue(new Callback<ResponseForgotPW>() {
            @Override
            public void onResponse(Call<ResponseForgotPW> call, Response<ResponseForgotPW> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getData().getToken(); // Đọc toàn bộ nội dung từ body của phản hồi

                    navigateToChangePWFragment(token);
                } else {
                    Log.e("User", "Error: " + response.body().getData().getMessage());
                    Log.e("User", "Error Code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseForgotPW> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }

    private void navigateToChangePWFragment(String token) {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        NewPasswordFragment newPasswordFragment= new NewPasswordFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        args.putString("username", username);
        newPasswordFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newPasswordFragment)
                .addToBackStack(null)
                .commit();
    }


    private void setEditTextListeners() {
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    editText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    editText6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}
