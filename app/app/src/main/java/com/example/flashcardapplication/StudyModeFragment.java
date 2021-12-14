package com.example.flashcardapplication;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardapplication.databinding.FragmentStudyModeBinding;
import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StudyModeFragment extends Fragment {

    private static final long SHAKE_DELAY  = 1500;

    private FragmentStudyModeBinding binding;
    private Deck deck;
    private Card currentCard;
    private MainActivity activity;
    private int index;
    private boolean isFront = true;
    private List<Card> cards;
    private List<Card> wrongCards;
    private int rightAnswers = 0;
    private AnimatorSet frontAnimation;
    private AnimatorSet backAnimation;

    private View view;
    private CardRecyclerViewAdapter adapter;

    private SensorManager sensorManager;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private long lastShake;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentStudyModeBinding.inflate(inflater, container, false);
        activity = (MainActivity) this.getActivity();
        deck = activity.getDeckViewModel().getDeck();
        index = 0;
        cards = new ArrayList<Card>(deck.getCards());
        wrongCards = new ArrayList<Card>();


        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(sensorManager).registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        lastShake = System.currentTimeMillis();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        binding.txtDeckTitle.setText(deck.getTitle());
        setupCardFlip(view);

        SetDataFields();

        binding.btnSkipQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cards.add(currentCard);
                SetDataFields();
            }
        });

        binding.btnWrongAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrongCards.add(currentCard);
                animate();
                SetDataFields();
            }
        });

        binding.btnRightAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightAnswers++;
                animate();
                SetDataFields();
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

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 5) {
                //Skip question on shake
                if(binding.layoutStudyQuestion.getVisibility() == View.VISIBLE && System.currentTimeMillis() - lastShake > SHAKE_DELAY)
                {
                    lastShake = System.currentTimeMillis();
                    cards.add(currentCard);
                    SetDataFields();
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public void onResume() {
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(sensorListener);
        super.onPause();
    }

    private void SetDataFields()
    {
        if(index < cards.size())
        {
            binding.layoutStudyResult.setVisibility(View.INVISIBLE);
            binding.layoutStudyAnswer.setVisibility(View.INVISIBLE);
            binding.layoutStudyQuestion.setVisibility(View.VISIBLE);
            currentCard = cards.get(index);
            binding.cardFront.setText(currentCard.getFront());
            index++;
        }
        else
        {
            //Show Results
            binding.layoutStudyAnswer.setVisibility(View.INVISIBLE);
            binding.layoutStudyQuestion.setVisibility(View.INVISIBLE);
            binding.cardFront.setVisibility(View.INVISIBLE);
            binding.cardBack.setVisibility(View.INVISIBLE);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cardList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new CardRecyclerViewAdapter(wrongCards, activity, true);
            recyclerView.setAdapter(adapter);

            binding.txtRightAnswers.setText("Number of right answers: " + rightAnswers);

            binding.txtWrongAnswers.setText("Number of wrong answers: " + wrongCards.size());

            double percentage = (double)rightAnswers/(double)(wrongCards.size() + rightAnswers) * 100;
            if(Double.isNaN(percentage))
                percentage = 0;

            binding.txtResultPercentage.setText(String.format("%.2f", percentage) + "%");
            binding.layoutStudyResult.setVisibility(View.VISIBLE);
        }
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

        frontAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animator);
        backAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animator);

        View.OnClickListener startAnimation = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate();
            }
        };
        front.setOnClickListener(startAnimation);
        back.setOnClickListener(startAnimation);
    }

    private void animate()
    {
        binding.layoutStudyAnswer.setVisibility(View.VISIBLE);
        binding.layoutStudyQuestion.setVisibility(View.INVISIBLE);
        binding.cardBack.setText(currentCard.getBack());

        if(isFront)
        {
            frontAnimation.setTarget(binding.cardFront);
            backAnimation.setTarget(binding.cardBack);
            frontAnimation.start();
            backAnimation.start();
            isFront = false;

        }
        else
        {
            frontAnimation.setTarget(binding.cardBack);
            backAnimation.setTarget(binding.cardFront);
            backAnimation.start();
            frontAnimation.start();
            isFront = true;

        }
    }

}