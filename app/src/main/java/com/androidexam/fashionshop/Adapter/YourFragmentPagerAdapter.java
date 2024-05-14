package com.androidexam.fashionshop.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.androidexam.fashionshop.Fragment.HistoryBuy.NotYetRatedFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.RatedFragment;

public class YourFragmentPagerAdapter extends FragmentStateAdapter {

    public YourFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new NotYetRatedFragment();
        } else {
            return new RatedFragment();
        }
    }

    @Override
    public int getItemCount() {
        // Số lượng trang
        return 2;
    }
}
