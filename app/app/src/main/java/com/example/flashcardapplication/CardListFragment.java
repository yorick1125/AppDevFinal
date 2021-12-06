package com.example.flashcardapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.flashcardapplication.databinding.FragmentCardListBinding;
import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.utils.DatePickerDialogFragment;
import com.example.flashcardapplication.utils.TimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 */
public class CardListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private Deck deck;
    private FragmentCardListBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CardListFragment newInstance(int columnCount) {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        deck = new Deck();
        //binding = FragmentCardListBinding.inflate(inflater, container, false);

        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cardList);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new CardRecyclerViewAdapter(Card.getDefaultCards()));
        //}

        ImageButton dueDateButton = (ImageButton) view.findViewById(R.id.dueDateButton);
        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                chooseDateAndTime(false, view);
            }
        });

        return view;
    }
    private void chooseDateAndTime(boolean istrue, View view){

        // default date and time
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        if(istrue){
            date = deck.getDueDate();
        }
        TextView dueDateTextView = (TextView) view.findViewById(R.id.dueDateTextView);
        dueDateTextView.setText(new SimpleDateFormat("EEEE").format(date) + ", " + new SimpleDateFormat("MMMM").format(date) + " " + new SimpleDateFormat("d").format(date) + " at " + new SimpleDateFormat("h:mm aa").format(date));
        deck.setDueDate(date);


        DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.create(date, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // set date to what user picked

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date date = calendar.getTime();
                deck.setDueDate(date);

                // check if date is old
                if(date.before(new Date())){
                    Toast.makeText(getContext(),"Can't be old date",Toast.LENGTH_LONG).show();
                }
                else{
                    // format the date
                    String dayFormat = new SimpleDateFormat("EEEE").format(date);
                    String monthFormat = new SimpleDateFormat("MMMM").format(date);
                    TimePickerDialogFragment timePickerDialogFragment = TimePickerDialogFragment.create(date, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                            // set time to what user picked
                            Date date1 = deck.getDueDate();
                            date1.setHours(hours);
                            date1.setMinutes(minutes);
                            //TextView dateTextView1 = (TextView) view.findViewById(R.id.dateTextView);
                            deck.setDueDate(date1);

                            // format the time
                            String time = new SimpleDateFormat("h:mm aa").format(date1);
                            dueDateTextView.setText(dayFormat + ", " + monthFormat + " " + day + " at " + time);
                        }
                    });
                    timePickerDialogFragment.show(getFragmentManager(),"timePicker");
                }

            }

        });
        datePickerDialogFragment.show(getFragmentManager(), "datePicker");

    }
}