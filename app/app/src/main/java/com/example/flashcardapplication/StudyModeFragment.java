package com.example.flashcardapplication;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.flashcardapplication.databinding.FragmentStudyModeBinding;
import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class StudyModeFragment extends Fragment {

    private FragmentStudyModeBinding binding;
    private Deck deck;
    private Card currentCard;
    private MainActivity activity;
    private int index;
    private boolean isFront;
    private List<Card> cards;
    
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentStudyModeBinding.inflate(inflater, container, false);
        activity = (MainActivity) this.getActivity();
        deck = activity.getDeckViewModel().getDeck();
        index = 0;

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFront = true;
        binding.txtDeckTitle.setText(deck.getTitle());
        setupCardFlip(view);

        //binding.cardFront.setText(deck.getCards().get(index).getFront());

        if(index < deck.getCards().size())
        {
            cards = new ArrayList<Card>(deck.getCards());
            currentCard = cards.get(index);
            binding.cardFront.setText(currentCard.getFront());
            index++;
        }

        binding.layoutStudyResult.setVisibility(View.INVISIBLE);
        binding.layoutStudyAnswer.setVisibility(View.INVISIBLE);
        binding.btnSkipQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutStudyAnswer.setVisibility(View.VISIBLE);
                binding.layoutStudyQuestion.setVisibility(View.INVISIBLE);
            }
        });

        binding.btnWrongAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutStudyAnswer.setVisibility(View.INVISIBLE);
                binding.layoutStudyQuestion.setVisibility(View.VISIBLE);
            }
        });

        binding.btnRightAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutStudyAnswer.setVisibility(View.INVISIBLE);
                binding.layoutStudyQuestion.setVisibility(View.INVISIBLE);
                binding.layoutStudyResult.setVisibility(View.VISIBLE);
            }
        });


        binding.btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudyModeFragment.this)
                        .navigate(R.id.action_studyModeFragment_to_homePageFragment);
            }
        });
    }

    private void SetDataFields()
    {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setupCardFlip(View view){
        Context applicationContext = getActivity().getApplicationContext();
        float scale = applicationContext.getResources().getDisplayMetrics().density;
        TextView front = view.findViewById(R.id.card_front);
        TextView back = view.findViewById(R.id.card_back);
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
    }

}