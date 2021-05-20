package com.example.lebonpetitcoin;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;

import com.google.firebase.database.annotations.NotNull;


public final class ProductGridItemDecoration extends ItemDecoration {
    private final int largePadding;
    private final int smallPadding;

    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull State state) {
        outRect.left = this.smallPadding;
        outRect.right = this.smallPadding;
        outRect.bottom=this.largePadding;
        outRect.top = this.largePadding;
    }

    public ProductGridItemDecoration(int largePadding, int smallPadding) {
        this.largePadding = largePadding;
        this.smallPadding = smallPadding;
    }
}
