package com.example.flashcardapplication;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.flashcardapplication.databinding.FragmentCardListBinding;
import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.utils.DatePickerDialogFragment;
import com.example.flashcardapplication.utils.TimePickerDialogFragment;
import com.example.flashcardapplication.viewmodel.DeckViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.SimpleDateFormat;
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

    private static int currentTaskNotificationId = 0;
    private MainActivity activity;
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;


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
        activity = (MainActivity) getActivity();
        deck = activity.getDeckViewModel().getDeck();
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
            /*
        List<Card> cards = null;
        try {
            cards = activity.getCardDBHandler().getCardTable().readAll();
            for(Card card : cards){
                if(card.getDeckId() != deck.getId()){
                    cards.remove(card);
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        try {
            recyclerView.setAdapter(new CardRecyclerViewAdapter(activity.getCardDBHandler().getCardTable().readAll()));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
             */

        recyclerView.setAdapter(new CardRecyclerViewAdapter(deck.getCards(), activity));

        ImageButton dueDateButton = (ImageButton) view.findViewById(R.id.dueDateButton);
        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                chooseDateAndTime(false, view);
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.addCardFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_cardListFragment_to_editCardFragment);
            }
        });

        EditText titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        titleEditText.setText(deck.getTitle());
        TextView dueDateTextView = (TextView) view.findViewById(R.id.dueDateTextView);
        dueDateTextView.setText(deck.getDueDate().toString());

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if(activity.getDeckViewModel().getState() == DeckViewModel.State.BEFORE_CREATE)
                    activity.getDeckViewModel().setState(DeckViewModel.State.CREATED);
                else if (activity.getDeckViewModel().getState() == DeckViewModel.State.BEFORE_EDIT)
                    activity.getDeckViewModel().setState(DeckViewModel.State.EDITED);
                activity.getDeckViewModel().setUpdatedDeck(deck);
                activity.getDeckViewModel().notifyChange();
                NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();

                // check if due date is past current date and check if it is null
                if( deck.getDueDate() != null && deck.getDueDate().getTime() > new Date().getTime()){
                    // override run so it does the notification once its past the due date
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(deck.getDueDate().getTime() - (new Date().getTime() - MILLIS_IN_A_DAY));
                                dayBeforeNotification();
                                Thread.sleep(deck.getDueDate().getTime() - new Date().getTime());
                                overdueNotification();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }

            }
        });





        return view;
    }

    private void overdueNotification(){

        // setting intent
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Task Id", deck.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        // making the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, MainActivity.DECK_OVERDUE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_priority_high_24)
                .setContentTitle(deck.getTitle())
                .setContentText("The due date for your deck has passed. How did your test go?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(currentTaskNotificationId++, builder.build());
    }

    private void dayBeforeNotification(){

        // setting intent
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Task Id", deck.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        // making the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder( activity, MainActivity.DECK_DUE_IN_DAY_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_priority_high_24)
                .setContentTitle(deck.getTitle())
                .setContentText("Your test for this deck is tomorrow! Why aren't you studying?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(currentTaskNotificationId++, builder.build());
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