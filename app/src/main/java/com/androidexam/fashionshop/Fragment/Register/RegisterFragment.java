package com.androidexam.fashionshop.Fragment.Register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Home.DetailProductFragment;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.AccessTokenResponse;
import com.androidexam.fashionshop.Model.User;
import com.androidexam.fashionshop.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int productId;
    public int UserId;
    private EditText nameEditText, emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private TextView registerButton;
    private TextView messageTextView;
    private TextView loginTextView;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        hideBottomNavigationView();
        nameEditText = view.findViewById(R.id.edt_name);
        usernameEditText = view.findViewById(R.id.edt_username);
        passwordEditText = view.findViewById(R.id.edt_password);
        passwordAgainEditText = view.findViewById(R.id.edt_passwordagain);
        registerButton = view.findViewById(R.id.btn_register);
        messageTextView = view.findViewById(R.id.tv_message);
        loginTextView = view.findViewById(R.id.tv_register);
        emailEditText = view.findViewById(R.id.edt_email);

        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getInt("product_id", -1);
            if (productId != -1) {

            } else {
            }
        }else productId =-1;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordAgain = passwordAgainEditText.getText().toString();
                String email = emailEditText.getText().toString();
                TextInputLayout nameTextInputLayout = view.findViewById(R.id.nameTextInputLayout);
                TextInputLayout usernameTextInputLayout = view.findViewById(R.id.usernameTextInputLayout);
                TextInputLayout passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
                TextInputLayout passwordAgainTextInputLayout = view.findViewById(R.id.passwordagainTextInputLayout);
                TextInputLayout emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);

                nameTextInputLayout.setErrorEnabled(false);
                emailTextInputLayout.setErrorEnabled(false);
                usernameTextInputLayout.setErrorEnabled(false);
                passwordTextInputLayout.setErrorEnabled(false);
                passwordAgainTextInputLayout.setErrorEnabled(false);


                boolean hasError = false;

                if (name.isEmpty()) {
                    nameTextInputLayout.setError("Vui lòng nhập tên.");
                    nameTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                } else if (name.length() > 100) {
                    nameTextInputLayout.setError("Tên quá dài, tối đa 100 ký tự.");
                    nameTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                }

                if (username.isEmpty()) {
                    usernameTextInputLayout.setError("Vui lòng nhập tên đăng nhập.");
                    usernameTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                } else if (username.length() < 5 || username.length() > 20) {
                    usernameTextInputLayout.setError("Tên đăng nhập phải có từ 5 đến 20 ký tự.");
                    usernameTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                }
                if (email.isEmpty()) {
                    emailTextInputLayout.setError("Vui lòng nhập email.");
                    emailTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                } else if (!email.endsWith("@gmail.com")) {
                    emailTextInputLayout.setError("Email chưa đúng định dạng @gmail.com.");
                    emailTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                } else {
                    emailTextInputLayout.setErrorEnabled(false);
                }
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

                    User user = new User(name, username, password, email);


                    ApiService apiService = ApiService.retrofit.create(ApiService.class);

                    apiService.registerUser(user).enqueue(new Callback<AccessTokenResponse>() {
                        @Override
                        public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                            if (response.isSuccessful()) {

                                AccessTokenResponse tokenResponse = response.body();
                                String accessToken = tokenResponse.getAccessToken();

                                String refreshToken = tokenResponse.getRefreshToken();
                                UserId = tokenResponse.getId();
                                String username = usernameEditText.getText().toString();
                                String password = passwordEditText.getText().toString();

                                SharedPreferences preferencesID = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorID = preferencesID.edit();
                                editorID.putInt("user_id", UserId);
                                editorID.apply();

                                SharedPreferences preferencesAC = requireContext().getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorAC = preferencesAC.edit();
                                editorAC.putString("accessToken", accessToken);
                                editorAC.apply();

                                SharedPreferences preferencesRF = requireContext().getSharedPreferences("REFRESH", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorRF = preferencesRF.edit();
                                editorRF.putString("refreshToken", refreshToken);
                                editorRF.apply();

                                messageTextView.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                                if (productId!=-1){
                                    DetailProductFragment fragment = DetailProductFragment.newInstance(productId);
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                                else {
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    HomeFragment homeFragment = new HomeFragment();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.fragment_container, homeFragment)
                                            .commit();
                                }


                            } else {
                                messageTextView.setVisibility(View.VISIBLE);
                                try {
                                    String errorBody = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(errorBody);

                                    // Kiểm tra xem khóa "error" có tồn tại không
                                    if (jsonObject.has("error")) {
                                        JSONObject errorObject = jsonObject.getJSONObject("error");

                                        // Kiểm tra xem khóa "additionalProp1" có tồn tại không
                                        if (errorObject.has("additionalProp1")) {
                                            String errorMessage = errorObject.getString("additionalProp1");

                                        } else {

                                        }
                                    } else {
                                        // Xử lý khi không có "error"
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                    // Xử lý khi có lỗi trong quá trình xử lý JSON hoặc đọc errorBody
                                    messageTextView.setText("Đăng ký thất bại. Vui lòng thử lại.");
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                            // Xảy ra lỗi khi gọi API
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText("Lỗi: " + t.getMessage());
                        }
                    });
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, LoginFragment.newInstance(username, password));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;

    }

    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }

    private void showBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }
}