package com.example.readify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstTimeFormActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cardViewBiography;
    CardView cardViewComputing;
    CardView cardViewCrime;
    CardView cardViewEducation;
    CardView cardViewFiction;
    CardView cardViewRomance;
    LinearLayout linearLayoutBiography;
    LinearLayout linearLayoutComputing;
    LinearLayout linearLayoutCrime;
    LinearLayout linearLayoutEducation;
    LinearLayout linearLayoutFiction;
    LinearLayout linearLayoutRomance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_form);

        //Defining a skip button
        TextView skipForm = findViewById(R.id.textSkipFirstForm);
        skipForm.setText(R.string.skip_first_form_message);
        skipForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstTimeFormActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Click on every cards and mark
        cardViewBiography = findViewById(R.id.cardViewBiography);
        linearLayoutBiography = findViewById(R.id.linearLayoutBiography);
        cardViewBiography.setOnClickListener(this);

        cardViewComputing = findViewById(R.id.cardViewComputing);
        linearLayoutComputing = findViewById(R.id.linearLayoutComputing);
        cardViewComputing.setOnClickListener(this);

        cardViewCrime = findViewById(R.id.cardViewCrime);
        linearLayoutCrime = findViewById(R.id.linearLayoutCrime);
        cardViewCrime.setOnClickListener(this);

        cardViewEducation = findViewById(R.id.cardViewEducation);
        linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        cardViewEducation.setOnClickListener(this);

        cardViewFiction = findViewById(R.id.cardViewFiction);
        linearLayoutFiction = findViewById(R.id.linearLayoutFiction);
        cardViewFiction.setOnClickListener(this);

        cardViewRomance = findViewById(R.id.cardViewRomance);
        linearLayoutRomance = findViewById(R.id.linearLayoutRomance);
        cardViewRomance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardViewBiography:
                checkCardView(cardViewBiography, linearLayoutBiography);
                break;
            case R.id.cardViewComputing:
                checkCardView(cardViewComputing, linearLayoutComputing);
                break;
            case R.id.cardViewCrime:
                checkCardView(cardViewCrime, linearLayoutCrime);
                break;
            case R.id.cardViewEducation:
                checkCardView(cardViewEducation, linearLayoutEducation);
                break;
            case R.id.cardViewFiction:
                checkCardView(cardViewFiction, linearLayoutFiction);
                break;
            case R.id.cardViewRomance:
                checkCardView(cardViewRomance, linearLayoutRomance);
                break;
        }
    }

    public void checkCardView(CardView cardView, LinearLayout linearLayout){
        if (cardView.getTag() == getString(R.string.cardView_unmark)) {
            cardView.setTag(getString(R.string.cardView_mark));
            linearLayout.setBackground(getDrawable(R.drawable.cardview_selected));
        } else {
            cardView.setTag(getString(R.string.cardView_unmark));
            linearLayout.setBackgroundColor(getColor(R.color.colorBlank));
        }
    }
}
