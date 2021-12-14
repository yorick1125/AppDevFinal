package com.example.flashcardapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.material.snackbar.Snackbar;

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
<<<<<<< Updated upstream

=======
        if(card.getUri() != null){
            selectedImage.setImageURI(card.getUri());
            selectedImage.setVisibility(View.VISIBLE);
        }
        else{
            selectedImage.setVisibility(View.GONE);
        }
>>>>>>> Stashed changes
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
                card.setFront(charSequence.toString());
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
                card.setBack(charSequence.toString());
                cardBack.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(card.getFront() == null || card.getFront().equals("") || card.getBack() == null || card.getBack().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("Question and Answer cannot be left empty");
                    alertDialogBuilder.setMessage("No new card will be created");
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
                            navController.popBackStack();
                            dialogInterface.cancel();
                        }
                    });
                    alertDialogBuilder.setCancelable(true);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return;
                }

                String message = "";
                if(activity.getCardViewModel().getState() == CardViewModel.State.BEFORE_CREATE) {
                    activity.getCardViewModel().setState(CardViewModel.State.CREATED);
                    message = "Card created successfully!";
                }
                else if (activity.getCardViewModel().getState() == CardViewModel.State.BEFORE_EDIT){
                    activity.getCardViewModel().setState(CardViewModel.State.EDITED);
                    message = "Card edited successfully!";
                }
                card.setDeckId(activity.getDeckViewModel().getDeck().getId());
                activity.getCardViewModel().setUpdatedCard(card);
                activity.getCardViewModel().notifyChange();
                NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();
                Snackbar snackbar = Snackbar
                        .make(activity.getBinding().getRoot(), message, 2000);

                snackbar.show();

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
<<<<<<< Updated upstream
            Bitmap image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
=======

            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag","Absolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getContext().sendBroadcast(mediaScanIntent);
                card.setUri(contentUri);
                System.out.println("setting camera uri");
            }
        }
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
               Uri contentUri = data.getData();
               String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
               Log.d("tag", "onActivityResult: Gallery Image Uri: " + imageFileName);
               selectedImage.setImageURI(contentUri);
               card.setUri(contentUri);
               System.out.println("setting gallery uri");
            }
>>>>>>> Stashed changes
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