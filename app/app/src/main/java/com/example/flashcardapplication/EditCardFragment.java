package com.example.flashcardapplication;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isFront;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCardFragment newInstance(String param1, String param2) {
        EditCardFragment fragment = new EditCardFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);
        // Inflate the layout for this fragment
        Context applicationContext = getActivity().getApplicationContext();
        float scale = applicationContext.getResources().getDisplayMetrics().density;
        TextView front = view.findViewById(R.id.card_front);
        TextView back = view.findViewById(R.id.card_back);
        isFront = true;
        if(front != null){
            front.setCameraDistance(8000 * scale);
        }
        if(back != null) {
            back.setCameraDistance(8000 * scale);
        }

        AnimatorSet frontAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator);
        AnimatorSet backAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator);

        View.OnClickListener startAnimation = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFront)
                {
                    frontAnimation.setTarget(front);
                    backAnimation.setTarget(back);
                    frontAnimation.start();
                    backAnimation.start();
                    isFront = false;

                }
                else
                {
                    frontAnimation.setTarget(back);
                    backAnimation.setTarget(front);
                    backAnimation.start();
                    frontAnimation.start();
                    isFront = true;

                }
            }
        };
        front.setOnClickListener(startAnimation);
        back.setOnClickListener(startAnimation);
        return view;
    }
}