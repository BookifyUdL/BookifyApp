package com.example.readify.Popups;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.EmojisAdapter;
import com.example.readify.Adapters.ShopsItemsVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Emoji;
import com.example.readify.Models.Item;
import com.example.readify.R;

import java.util.ArrayList;

public class ShopsPopup extends DialogFragment implements Popup {

    public ShopsPopup(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shops_popup, container);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton closeArrow = (ImageButton) view.findViewById(R.id.close_arrow);
        closeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        RecyclerView recyclerViewShops = view.findViewById(R.id.shops_recycler_view);
        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewShops.setLayoutManager(vlm);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 4);
        //recyclerViewShops.setLayoutManager(gridLayoutManager);

        ArrayList<Item> items= MockupsValues.getItems();

        //EmojisAdapter emojisAdapter = new EmojisAdapter(getContext(), emojis);
        ShopsItemsVerticalAdapter adapter = new ShopsItemsVerticalAdapter(getContext(), items);
        recyclerViewShops.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //float dim = ViewGroup.LayoutParams.WRAP_CONTENT - getContext().getResources().getDimension(R.dimen.margin_10dp);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void close() {
        this.dismiss();
    }
}
