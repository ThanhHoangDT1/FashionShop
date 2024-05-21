package com.androidexam.fashionshop.Fragment.Pay;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.R;

public class WebViewFragment extends Fragment {

    private WebView webView;
    private ImageButton back;

    public WebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    public void handleBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = view.findViewById(R.id.webView);

        back = view.findViewById(R.id.back_button_pay);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHome();
                Toast.makeText(getContext(), "Thanh toán không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());


        Bundle bundle = getArguments();
        if (bundle == null) {

            return;
        }

        String url = bundle.getString("url");
        if (url != null) {
            Log.d("URL", url);
            webView.loadUrl(url);
        } else {
        }

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    // Nếu có thể quay lại, quay lại trang trước đó trong WebView
                    webView.goBack();
                    return true; // Đã xử lý sự kiện
                } else {
                    onHome();
                    return true;
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Kiểm tra xem URL có phải là "https://sub.tuoimakeup.com/order?state=1" không
                if (url.equals("http://192.168.2.98:5000/vnpay_return?state=1")) {

                    onHome();
                    Toast.makeText(getContext(), "Thanh toán thành công.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (url.equals("http://192.168.2.98:5000/vnpay_return?state=0")){
                    onHome();
                    Toast.makeText(getContext(), "Thanh toán không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });



    }
    private void onHome() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }
}
