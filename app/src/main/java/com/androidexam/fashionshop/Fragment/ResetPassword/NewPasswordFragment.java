package com.androidexam.fashionshop.Fragment.ResetPassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.androidexam.fashionshop.Model.NewPassword;
import com.androidexam.fashionshop.R;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordFragment extends Fragment {

    private EditText passwordedt;
    private EditText passwordAgainedt;
    private TextView btnContinue;
    private ImageButton back;


    private String username, token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_password, container, false);

        passwordedt = view.findViewById(R.id.edt_password_new);
        passwordAgainedt =  view.findViewById(R.id.edt_passwordagain_new);
        btnContinue =  view.findViewById(R.id.btn_change_access);

        back = view.findViewById(R.id.back_newpw_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAccess(view);
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username", null);
            token = bundle.getString("token", null);

        }

        return view;
    }

    private void ChangeAccess(View view) {
        boolean hasError = false;
        String password = passwordedt.getText().toString();
        String passwordAgain = passwordAgainedt.getText().toString();
        TextInputLayout passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        TextInputLayout passwordAgainTextInputLayout = view.findViewById(R.id.passwordagainTextInputLayout);
        passwordTextInputLayout.setErrorEnabled(false);
        passwordAgainTextInputLayout.setErrorEnabled(false);
        if (password.isEmpty()) {
            passwordTextInputLayout.setError("Vui lòng nhập mật khẩu.");
            passwordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        } else if (password.length() < 8 || password.length() > 20) {
            passwordTextInputLayout.setError("Mật khẩu phải có từ 8 đến 20 ký tự.");
            passwordTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (passwordAgain.isEmpty()) {
            passwordAgainTextInputLayout.setError("Vui lòng nhập lại mật khẩu.");
            passwordAgainTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (!password.equals(passwordAgain)) {
            passwordAgainTextInputLayout.setError("Mật khẩu không khớp.");
            passwordAgainTextInputLayout.setErrorEnabled(true);
            hasError = true;
        }

        if (!hasError) {
            NewPassword newPassword=new NewPassword(token,password);
            ApiService.productService.resetPassword(newPassword).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(),"Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            navigateToLoginFragment();
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
    }
    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
    private void navigateToEnterOTPFragment(String email) {



        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        EnterOTPFragment enterOTPFragment= new EnterOTPFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        enterOTPFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, enterOTPFragment)
                .addToBackStack(null)
                .commit();

    }
    private void navigateToLoginFragment() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();
    }

}
