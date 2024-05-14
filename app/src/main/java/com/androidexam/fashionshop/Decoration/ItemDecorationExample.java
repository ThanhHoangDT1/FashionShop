package com.androidexam.fashionshop.Decoration;
import android.graphics.Rect;
        import android.view.View;
        import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.fashionshop.Fragment.Home.DetailContentFragment;

public class ItemDecorationExample extends RecyclerView.ItemDecoration {
    private final int spacing;

    public ItemDecorationExample(DetailContentFragment context, int spacing) {
        this.spacing = context.getResources().getDimensionPixelSize(spacing);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.top = spacing;
        outRect.bottom = spacing;
    }
}
