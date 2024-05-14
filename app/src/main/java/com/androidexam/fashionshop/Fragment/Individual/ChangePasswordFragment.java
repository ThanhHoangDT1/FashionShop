package com.androidexam.fashionshop.Fragment.Individual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Model.ChangePassword;
import com.androidexam.fashionshop.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {



    private EditText currentPassword,passwordAgain,newPassword;
    private TextView btnContinue;
    private ImageButton back;
    private int userId;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_change_password, container, false);
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        currentPassword = view.findViewById(R.id.edt_current_password);
        newPassword = view.findViewById(R.id.edt_new_password);
        passwordAgain =  view.findViewById(R.id.edt_passwordagain_new);
        btnContinue =  view.findViewById(R.id.btn_change_access);
        back = view.findViewById(R.id.back_changepw_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFragment();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Continute(view);
            }
        });


        return view;
    }

    private void Continute(View view) {
        boolean hasError = false;
        String newpassword = newPassword.getText().toString();
        String currpassword = currentPassword.getText().toString() ;
        String passwordagain = passwordAgain.getText().toString();
        TextInputLayout newPasswordTextInputLayout = view.findViewById(R.id.newPasswordTextInputLayout);
        TextInputLayout curPasswordTextInputLayout = view.findViewById(R.id.currentPasswordTextInputLayout);
        TextInputLayout passwordAgainTextInputLayout = view.findViewById(R.id.passwordagainTextInputLayout);
        newPasswordTextInputLayout.setErrorEnabled(false);
        curPasswordTextInputLayout.setErrorEnabled(false);
        passwordAgainTextInputLayout.setErrorEnabled(false);
        if (newpassword.isEmpty()) {
            newPasswordTextInputLayout.setError("Vui lòng nhập mật khẩu mới.");
            newPasswordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        } else if (newpassword.length() < 8 || newpassword.length() > 20) {
            newPasswordTextInputLayout.setError("Mật khẩu phải có từ 8 đến 20 ký tự.");
            newPasswordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }
        if (currpassword.isEmpty()) {
            curPasswordTextInputLayout.setError("Vui lòng nhập mật khẩu hiện tại.");
            curPasswordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        } else if (currpassword.length() < 8 || currpassword.length() > 20) {
            curPasswordTextInputLayout.setError("Mật khẩu phải có từ 8 đến 20 ký tự.");
            curPasswordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (passwordagain.isEmpty()) {
            passwordAgainTextInputLayout.setError("Vui lòng nhập lại mật khẩu.");
            passwordAgainTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }
        if (newpassword.equals(currpassword)==true) {
            newPasswordTextInputLayout.setError("Mật khẩu mới không được trùng với mật khẩu hiện tại.");
            newPasswordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (!newpassword.equals(passwordagain)) {
            passwordAgainTextInputLayout.setError("Mật khẩu không khớp.");
            passwordAgainTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (!hasError) {
            ChangePassword changePassword=new ChangePassword(currpassword,newpassword,passwordagain);
            ApiService.productServiceWithToken.changePassword(userId,changePassword).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        backFragment();
                    } else {
                        try {
                            String errorBodyString = response.errorBody().string();

                            JsonObject errorJson = new Gson().fromJson(errorBodyString, JsonObject.class);


                            String errorMessage = errorJson.getAsJsonObject("error").get("message").getAsString();

                            curPasswordTextInputLayout.setError( errorMessage);
                            curPasswordTextInputLayout.setErrorEnabled(true);
                            Log.e("User", "Error: " + errorMessage);
                            Log.e("User", "Error Code: " + response.code());

                            System.out.println("Error Body: " + errorMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }



                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("User", "API Call Failed: " + t.getMessage());
                }
            });
        }
    }

    private void backFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}