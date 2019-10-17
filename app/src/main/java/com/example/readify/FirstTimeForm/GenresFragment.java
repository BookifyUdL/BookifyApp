package com.example.readify.FirstTimeForm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readify.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GenresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View view;
    Activity activity;
    ComunicateFragmentsFirstForm comunicateFragmentsFirstForm;

    //CarouselView
    CarouselView carouselView;
    int NUMBER_OF_FORMS = 2;

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

    public GenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_genres, container, false);
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setFillColor(R.color.default_circle_indicator_stroke_color);

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(NUMBER_OF_FORMS);
        carouselView.setSlideInterval(0);
        carouselView.setViewListener(viewListener);

        // Inflate the layout for this fragment
        return view;
    }

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View view = null;

            switch (position){
                case 0:
                    view = getLayoutInflater().inflate(R.layout.fist_form_genres, null);
                    //set view attributes here

                    //Defining a skip button
                    TextView skipForm = view.findViewById(R.id.textSkipFirstForm);
                    skipForm.setText(R.string.skip_first_form_message);
                    skipForm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            comunicateFragmentsFirstForm.exitForm();
                        }
                    });

                    //Click on every cards and mark
                    cardViewBiography = view.findViewById(R.id.cardViewBiography);
                    linearLayoutBiography = view.findViewById(R.id.linearLayoutBiography);
                    cardViewBiography.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkCardView(cardViewBiography, linearLayoutBiography);
                        }
                    });
                    break;
                case 1:
                    //Libros leídos
                    view = getLayoutInflater().inflate(R.layout.fragment_discover, null);
                    break;
                    //Libros para leer
                /*case 2:

                    break;*/
            }

            /*cardViewComputing = view.findViewById(R.id.cardViewComputing);
            linearLayoutComputing = view.findViewById(R.id.linearLayoutComputing);
            cardViewComputing.setOnClickListener(this);

            cardViewCrime = view.findViewById(R.id.cardViewCrime);
            linearLayoutCrime = view.findViewById(R.id.linearLayoutCrime);
            cardViewCrime.setOnClickListener(this);

            cardViewEducation = view.findViewById(R.id.cardViewEducation);
            linearLayoutEducation = view.findViewById(R.id.linearLayoutEducation);
            cardViewEducation.setOnClickListener(this);

            cardViewFiction = view.findViewById(R.id.cardViewFiction);
            linearLayoutFiction = view.findViewById(R.id.linearLayoutFiction);
            cardViewFiction.setOnClickListener(this);

            cardViewRomance = view.findViewById(R.id.cardViewRomance);
            linearLayoutRomance = view.findViewById(R.id.linearLayoutRomance);
            cardViewRomance.setOnClickListener(this);*/

            return view;
        }

        void checkCardView(CardView cardView, LinearLayout linearLayout) {
            if (cardView.getTag() == getString(R.string.cardView_unmark)) {
                cardView.setTag(getString(R.string.cardView_mark));
                linearLayout.setBackground(view.getContext().getDrawable(R.drawable.cardview_selected));
            } else {
                cardView.setTag(getString(R.string.cardView_unmark));
                linearLayout.setBackgroundColor(view.getContext().getColor(R.color.colorBlank));
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            comunicateFragmentsFirstForm = (ComunicateFragmentsFirstForm) activity;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
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
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
