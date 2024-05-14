package com.androidexam.fashionshop.Decoration;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ColumnSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public ColumnSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 2 != 0) {
            outRect.left = spacing;
        }
    }
}

