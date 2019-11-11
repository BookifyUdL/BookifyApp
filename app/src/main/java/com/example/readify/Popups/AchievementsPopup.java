package com.example.readify.Popups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.AchievementsVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.User;
import com.example.readify.R;

public class AchievementsPopup extends DialogFragment implements Popup {

    private View view;
    private FragmentManager fragmentManager;
    private AchievementsVerticalAdapter achievementsVerticalAdapter;
    private User user;
    private SharedPreferences prefs;

    public AchievementsPopup(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.achievements_popup, container);
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = new User();

        //Button close
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.close_achievements_popup);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        //RecyclerView with achievements
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_achievements_popup);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        achievementsVerticalAdapter = new AchievementsVerticalAdapter(getContext(), user.getAchievements());
        recyclerView.setAdapter(achievementsVerticalAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        user.readFromSharedPreferences(prefs);
        achievementsVerticalAdapter.setAchivementsList(user.getAchievements());
        achievementsVerticalAdapter.notifyDataSetChanged();
    }

    @Override
    public void close() {
        this.dismiss();
    }
}
