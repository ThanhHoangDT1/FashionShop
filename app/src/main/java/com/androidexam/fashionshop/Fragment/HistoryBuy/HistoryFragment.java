package com.androidexam.fashionshop.Fragment.HistoryBuy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.androidexam.fashionshop.Adapter.HistoryAdapter;
import com.androidexam.fashionshop.Adapter.YourFragmentPagerAdapter;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HistoryFragment extends Fragment {




    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public HistoryFragment() {

    }


    private ImageButton back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        back= view.findViewById(R.id.back_button_history);
        viewPager2 = view.findViewById(R.id.viewPagerhis);
        HistoryAdapter adapter = new HistoryAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        hideBottomNavigationView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
         tabLayout = view.findViewById(R.id.tabLayoutHis);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đã xác nhận");
                    break;
                case 1:
                    tab.setText("Đang giao");
                    break;
                case 2:
                    tab.setText("Đã giao");
                    break;
                case 3:
                    tab.setText("Đã hủy");
                    break;
            }
        }).attach();



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

    public void goToTab(int position) {

        if(tabLayout!=null){
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (tab != null) {
                tab.select();
            }
        }

    }
}