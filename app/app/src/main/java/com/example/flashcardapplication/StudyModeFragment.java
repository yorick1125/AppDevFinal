package com.example.flashcardapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.flashcardapplication.databinding.FragmentStudyModeBinding;

public class StudyModeFragment extends Fragment {

    private FragmentStudyModeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentStudyModeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}