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
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.EmojisAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Emoji;
import com.example.readify.R;

import java.util.ArrayList;

public class ShopsPopup extends DialogFragment implements Popup {

    public ShopsPopup(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_readed_popup, container);


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
