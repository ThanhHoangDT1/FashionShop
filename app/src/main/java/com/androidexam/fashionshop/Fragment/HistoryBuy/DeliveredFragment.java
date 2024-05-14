package com.androidexam.fashionshop.Fragment.HistoryBuy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.androidexam.fashionshop.Adapter.YourFragmentPagerAdapter;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DeliveredFragment extends Fragment {

    private ImageButton back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        back= view.findViewById(R.id.back_button_rate);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager2);
        YourFragmentPagerAdapter adapter = new YourFragmentPagerAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        hideBottomNavigationView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            // Thiết lập tên cho mỗi tab dựa trên vị trí (position)
            switch (position) {
                case 0:
                    tab.setText("Chưa đánh giá");
                    break;
                case 1:
                    tab.setText("Đã đánh giá");
                    break;
                // Thêm các trường hợp khác nếu có nhiều hơn 2 tab
            }
        }).attach();

        // Bắt sự kiện lướt trang
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Trang mới được chọn
                if (position == 0) {
                    // Trang A được chọn
                    // Thực hiện các xử lý khi chuyển sang trang A
                } else if (position == 1) {
                    // Trang B được chọn
                    // Thực hiện các xử lý khi chuyển sang trang B
                }
            }
        });

        return view;
    }
    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }

}
