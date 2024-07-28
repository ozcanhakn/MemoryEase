package app.forget.forgetfulnessapp.Manager;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class RandomLayoutManager extends RecyclerView.LayoutManager {

    public RandomLayoutManager(Context context) {
        // Initialize if needed
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        int parentWidth = getWidth();
        int parentHeight = getHeight();
        int itemCount = getItemCount();

        if (itemCount == 0) {
            return;
        }

        int rows = (itemCount <= 4) ? 2 : 3; // For 4 items or less, use 2 rows; otherwise, use 3 rows
        int cols = (itemCount + rows - 1) / rows; // Calculate number of columns based on items and rows
        int itemWidth = parentWidth / cols;
        int itemHeight = parentHeight / rows;

        for (int i = 0; i < itemCount; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            int col = i % cols;
            int row = i / cols;
            int left = col * itemWidth + (itemWidth - width) / 2;
            int top = row * itemHeight + (itemHeight - height) / 2;

            layoutDecorated(view, left, top, left + width, top + height);
        }
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}