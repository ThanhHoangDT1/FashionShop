package com.androidexam.fashionshop.Adapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.androidexam.fashionshop.Fragment.Home.DetailContentFragment;

import java.util.List;

public class ProductImagePagerAdapter extends FragmentPagerAdapter {
    private List<String> imageUrls;

    public ProductImagePagerAdapter(FragmentManager fm, List<String> imageUrls) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.imageUrls = imageUrls;
    }

    @Override
    public Fragment getItem(int position) {

        return DetailContentFragment.ImageFragment.newInstance(imageUrls.get(position));
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }
}

