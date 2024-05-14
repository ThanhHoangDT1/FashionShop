package com.androidexam.fashionshop.Fragment.LogIn;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.ResetPassword.ForgotPassWordFragment;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.AccessTokenResponse;
import com.androidexam.fashionshop.Model.User;
import com.androidexam.fashionshop.R;
import com.androidexam.fashionshop.Fragment.Register.RegisterFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText edtUsername;
    private int productId;
    private EditText edtPassword;
    public int UserId;
    private TextView messageTextView;
    private TextView btnLoin,tvRegister,tvForgotPW;
    public String accessToken, refreshToken;

    private ImageView loginGoogle;


    public LoginFragment() {
        // Required empty public constructor
    }


    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getInt("product_id", -1);
            if (productId != -1) {

            } else {
            }
        }else productId=-1;
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        hideBottomNavigationView();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(getString(R.string.default_web_client_id)) // Thêm dòng này
//                .build();

         gsc = GoogleSignIn.getClient(getContext(),gso);

        loginGoogle = view.findViewById(R.id.login_gg);
        edtUsername = view.findViewById(R.id.edt_us);
        edtPassword = view.findViewById(R.id.edt_pw);
        messageTextView = view.findViewById(R.id.tv_message);
        tvForgotPW = view.findViewById(R.id.tv_forgot_password);
        btnLoin = view.findViewById(R.id.btn_login);

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });





//        btnLoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });

        btnLoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("DA kich", "onResponse: ");
//                String user_id = edtUsername.getText().toString();
//                ApiService.productService.test(Integer.parseInt(user_id)).enqueue(new Callback<test>() {
//                    @Override
//                    public void onResponse(Call<test> call, Response<test> response) {
//                        Log.d("THANHCONG", "onResponse: Thanh cong roi");
//                    }
//
//                    @Override
//                    public void onFailure(Call<test> call, Throwable t) {
//                        Log.d("Fail roi", "onResponse: "+t.getMessage());
//                    }
//                });

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                TextInputLayout usernameTextInputLayout = view.findViewById(R.id.userTextInputLayout);
                TextInputLayout passwordTextInputLayout = view.findViewById(R.id.psTextInputLayout);
                usernameTextInputLayout.setErrorEnabled(false);
                passwordTextInputLayout.setErrorEnabled(false);

                boolean hasError = false;


                if (username.isEmpty()) {
                    usernameTextInputLayout.setError("Vui lòng nhập tên đăng nhập.");
                    usernameTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                }
                if (password.isEmpty()) {
                    passwordTextInputLayout.setError("Vui lòng nhập mật khẩu.");
                    passwordTextInputLayout.setErrorEnabled(true);
                    hasError = true;
                }
                Log.d("hasError", ""+hasError);

                if (!hasError) {

                User user = new User(username, password);
                ApiService.productService.loginUser(user).enqueue(new Callback<AccessTokenResponse>() {

                    @Override
                    public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {

                        if (response.isSuccessful()) {
                            AccessTokenResponse tokenResponse = response.body();
                            accessToken = tokenResponse.getAccessToken();
                            refreshToken = tokenResponse.getRefreshToken();
                            UserId = tokenResponse.getId();
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
                            Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            navigateToHome();


                        } else {
                            messageTextView.setVisibility(View.VISIBLE);
                            try {
                                // Lấy thông điệp lỗi từ phản hồi
                                String errorMessage = response.errorBody().string();
                                // Nếu thông điệp lỗi là JSON, bạn có thể sử dụng một thư viện JSON để phân tích cú pháp
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");

                                // Hiển thị thông báo lỗi hoặc xử lý theo nhu cầu của bạn
                                Log.e("LoginFragment", "Login failed: " + message);
                                // Bạn có thể hiển thị thông báo lỗi cho người dùng
                                messageTextView.setText(message);
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                        t.printStackTrace();
                        Log.d("FAIL", "onFailure: DA FAIL "+t.getMessage());
                        Log.d("user", "onFailure: "+user.getClass());

                    }
                });

            }

            }
        });


        tvRegister = view.findViewById(R.id.tv_register);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToRegisterFragment();
            }
        });

        tvForgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToForgotPWFragment();
            }
        });
        return view;
    }

    private void signOut() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        navigateToHome();
                    }
                });
    }


    private void navigateToForgotPWFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ForgotPassWordFragment forgotPassWordFragment= new ForgotPassWordFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, forgotPassWordFragment)
                .addToBackStack(null)
                .commit();

    }

    private void navigateToRegisterFragment() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        RegisterFragment registerFragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putInt("product_id", productId);
        registerFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }


    private void navigateToHome() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
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

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 11);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handle(task);
        }

    }

    private void handle(Task<GoogleSignInAccount> task) {
     GoogleSignInAccount account=task.getResult();
     if (account!=null){
         Uri photoUrl = account.getPhotoUrl();
         if (photoUrl != null) {
             String avatarUrl = photoUrl.toString();
             String name = account.getDisplayName();
             String gmail = account.getEmail();

             ApiService.productService.gg(gmail, name, avatarUrl).enqueue(new Callback<AccessTokenResponse>() {
                 @Override
                 public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                     if(response.isSuccessful()){
                         AccessTokenResponse tokenResponse = response.body();
                         accessToken = tokenResponse.getAccessToken();
                         refreshToken = tokenResponse.getRefreshToken();
                         UserId = tokenResponse.getId();
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
                         Toast.makeText(getContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                         navigateToHome();

                     }

                 }

                 @Override
                 public void onFailure(Call<AccessTokenResponse> call, Throwable t) {

                 }
             });


         }
     }
    }
    @Override
    public void onResume() {
        super.onResume();


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });
    }



}