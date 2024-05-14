package com.androidexam.fashionshop.Fragment.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidexam.fashionshop.Adapter.CommentAdapter;
import com.androidexam.fashionshop.Decoration.ItemDecorationExample;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.Comment;
import com.androidexam.fashionshop.Model.Product_Detail;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class DetailContentFragment extends Fragment {
    private AlertDialog alertDialog;
    private TextView productName;
    private TextView productPrice;
    private ViewPager viewPager;
    private RecyclerView recyclerView;

    private TextView productPriceOld;
    private TextView soldQuantity;
    private RatingBar ratebar;
    private TextView productDescription,bonusview,d;
    private boolean isExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_product_content, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        productName = view.findViewById(R.id.product_name);
        productPrice = view.findViewById(R.id.product_price);
        productPriceOld = view.findViewById(R.id.product_price_old);
        soldQuantity = view.findViewById(R.id.sold_quantity);
        productDescription = view.findViewById(R.id.product_description);
        recyclerView = view.findViewById(R.id.rcw_comment);
        ratebar = view.findViewById(R.id.ratingBar);
        d = view.findViewById(R.id.d);
        bonusview = view.findViewById(R.id.view);
        return view;
    }

    public void setProductDetail(Product_Detail productDetail) {
        if (productDetail != null) {
            DetailContentFragment.ImageFragmentAdapter adapter = new ImageFragmentAdapter(getChildFragmentManager(), productDetail.getProductUrls());
            viewPager.setAdapter(adapter);
            productName.setText(productDetail.getProductName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            productPrice.setText("đ" + decimalFormat.format(productDetail.getPrice_promote()));

            soldQuantity.setText("Đã bán: " + productDetail.getQuantity_sold());


            if(productDetail.getPrice_promote()==productDetail.getPrice()){
                productPriceOld.setVisibility(View.GONE);
                d.setVisibility(View.GONE);
            }else {
                d.setVisibility(View.VISIBLE);
                productPriceOld.setVisibility(View.VISIBLE);
                String originalPrice = String.valueOf(decimalFormat.format(productDetail.getPrice()));
                SpannableString spannableString = new SpannableString(originalPrice);
                spannableString.setSpan(new StrikethroughSpan(), 0, originalPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                productPriceOld.setText(spannableString);
            }

            bonusview.setText("Xem thêm");
            productDescription.setMaxLines(20);
            productDescription.setEllipsize(TextUtils.TruncateAt.END);
            productDescription.setText(Html.fromHtml(productDetail.getDescription()));
            bonusview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    toggleDescription(productDetail.getDescription());
                }
            });




            List<Comment> comments = productDetail.getComments();
            CommentAdapter commentAdapter = new CommentAdapter(comments, requireContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(commentAdapter);
            ItemDecorationExample itemDecoration = new ItemDecorationExample(this, R.dimen.spacing);
            recyclerView.addItemDecoration(itemDecoration);
            ratebar.setRating(productDetail.getAvgRate());

            List<String> sizeList = productDetail.getSizeNames();
            if (alertDialog != null && alertDialog.isShowing()) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizeList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                Spinner spinnerSize = alertDialog.findViewById(R.id.spinnerSize);
                if (spinnerSize != null) {
                    spinnerSize.setAdapter(spinnerAdapter);
                }
            }

        }
    }



    private static class ImageFragmentAdapter extends FragmentStatePagerAdapter {
        private List<String> imageUrls;

        public ImageFragmentAdapter(FragmentManager fm, List<String> imageUrls) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.imageUrls = imageUrls;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(imageUrls.get(position));
        }

        @Override
        public int getCount() {
            if (imageUrls != null) {
                return imageUrls.size();
                // Tiếp tục xử lý
            } else {
                return 0;
            }
        }
    }

    public static class ImageFragment extends Fragment {
        private static final String IMAGE_URL_KEY = "image_url";
        private ImageView imageView;

        public static ImageFragment newInstance(String imageUrl) {
            Bundle args = new Bundle();
            args.putString(IMAGE_URL_KEY, imageUrl);
            ImageFragment fragment = new ImageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_image, container, false);
            imageView = view.findViewById(R.id.imageView);
            hideBottomNavigationView();
            String imageUrl = getArguments().getString(IMAGE_URL_KEY);
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(imageView);
            }
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
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
    public void toggleDescription(String des ) {
        if (isExpanded==true) {
            bonusview.setText("Rút gọn");
            productDescription.setMaxLines(Integer.MAX_VALUE);
            productDescription.setEllipsize(null);

            productDescription.setText(Html.fromHtml(des));
           // descriptionTextView.setText("Nội dung mô tả dài...");
        } else {
            bonusview.setText("Xem thêm");
            productDescription.setMaxLines(20);
            productDescription.setEllipsize(TextUtils.TruncateAt.END);
            productDescription.setText(Html.fromHtml(des));
        }

        isExpanded= !isExpanded;
    }
}


