package com.example.flashcardapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.TextView;

import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.viewmodel.DeckViewModel;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCardFragment extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView selectedImage;
    Button cameraBtn,galleryBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isFront = true;
    private Card card;
    private MainActivity activity;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);
        activity = (MainActivity) getActivity();
        card = activity.getCardViewModel().getCard();
        selectedImage = view.findViewById(R.id.cardImageView);
        cameraBtn = view.findViewById(R.id.cameraBtn);
        galleryBtn = view.findViewById(R.id.galleryBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext(), "Gallery Button is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        setupCardFlip(view);

        EditText editTextQuestion = (EditText) view.findViewById(R.id.editTextQuestion);
        EditText editTextAnswer = (EditText) view.findViewById(R.id.editTextAnswer);
        TextView cardFront = (TextView) view.findViewById(R.id.card_front);
        TextView cardBack = (TextView) view.findViewById(R.id.card_back);
        if(card != null){
            if(card.getFront() != null){
                cardFront.setText(card.getFront());
                editTextQuestion.setText(card.getFront());
            }
            if(card.getBack() != null){
                cardBack.setText(card.getBack());
                editTextAnswer.setText(card.getBack());
            }
        }
        editTextQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activity.getCardViewModel().getCard().setFront(charSequence.toString());
                //activity.getCardViewModel().notifyChange();
                cardFront.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activity.getCardViewModel().getCard().setBack(charSequence.toString());
                //activity.getCardViewModel().notifyChange();
                cardBack.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if(activity.getCardViewModel().getState() == CardViewModel.State.BEFORE_CREATE) {
                    activity.getCardViewModel().setState(CardViewModel.State.CREATED);
                }
                else if (activity.getCardViewModel().getState() == CardViewModel.State.BEFORE_EDIT){
                    activity.getCardViewModel().setState(CardViewModel.State.EDITED);
                }
                activity.getCardViewModel().setUpdatedCard(card);
                activity.getCardViewModel().notifyChange();
                NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();


            }
        });

        return view;
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            openCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length < 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(getContext(), "Camera Permission is Required to Use camera", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
        }

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