package com.androidexam.fashionshop.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.androidexam.fashionshop.Fragment.HistoryBuy.CancelFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.ConfirmedFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.DelivedFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.HistoryFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.InTransitFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.NotYetRatedFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.RatedFragment;

public class HistoryAdapter extends FragmentStateAdapter {
    public HistoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ConfirmedFragment();
        } else if (position == 1) {
            fragment = new InTransitFragment();
        } else if (position == 2) {
            fragment = new DelivedFragment();
        } else {
            fragment = new CancelFragment();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
