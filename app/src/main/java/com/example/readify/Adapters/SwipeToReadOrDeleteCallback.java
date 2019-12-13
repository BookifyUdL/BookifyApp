package com.example.readify.Adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.R;

public class SwipeToReadOrDeleteCallback extends ItemTouchHelper.SimpleCallback {

    public BooksListVerticalAdapter mAdapter;
    private Drawable icon, delete, action;
    private ColorDrawable background;
    private boolean toPending;
    ColorDrawable red = new ColorDrawable(Color.RED);
    ColorDrawable yellow = new ColorDrawable(Color.parseColor("#d9c01c"));
    ColorDrawable green = new ColorDrawable(Color.parseColor("#5BE356"));

    public SwipeToReadOrDeleteCallback(BooksListVerticalAdapter adapter, boolean pending){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        icon = delete = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_delete_white);
        toPending = pending;
        if(pending){
            action = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_reading_white);
        } else {
            action = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_pending_white);
        }
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            mAdapter.deleteItem(position);
        } else {
            if(toPending)
                mAdapter.readingListChanged(position);
            if(!toPending)
                mAdapter.pendingListChanged(position);
            // Add to reading list
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        if (dX > 0) { // Swiping to the right
            if(toPending)
                background = green;
            if(!toPending)
                background = yellow;
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());

        } else if (dX < 0) { // Swiping to the left
            background = red;
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            icon = action;
            int iconLeft = itemView.getLeft() + iconMargin;
            //int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

        } else if (dX < 0) { // Swiping to the left
            icon = delete;
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
