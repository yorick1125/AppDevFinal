package com.example.flashcardapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.flashcardapplication.databinding.FragmentCardListBinding;
import com.example.flashcardapplication.enums.Subjects;
import com.example.flashcardapplication.model.Card;
import com.example.flashcardapplication.model.CardTable;
import com.example.flashcardapplication.model.Deck;
import com.example.flashcardapplication.model.DeckTable;
import com.example.flashcardapplication.sqlite.DatabaseException;
import com.example.flashcardapplication.utils.DatePickerDialogFragment;
import com.example.flashcardapplication.utils.TimePickerDialogFragment;
import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.viewmodel.DeckViewModel;
import com.example.flashcardapplication.viewmodel.ObservableModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

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
    private CardRecyclerViewAdapter adapter;

    private static int currentTaskNotificationId = 0;
    private MainActivity activity;
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    private EditText title;
    private Spinner description;
    private TextView date;
    private Switch calendarSwitch;

    private boolean dateChanged;


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
        adapter = new CardRecyclerViewAdapter(deck.getCards(), activity);
        recyclerView.setAdapter(adapter);

        Spinner spin = (Spinner) view.findViewById(R.id.subjectSpinner);
        ArrayAdapter<Subjects> adapter = new ArrayAdapter<Subjects>(activity, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Subjects.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                deck.setSubject((Subjects) parentView.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

        ImageButton dueDateButton = (ImageButton) view.findViewById(R.id.dueDateButton);
        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ");
                chooseDateAndTime(false, view);
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.addCardFab);

        if(activity.getDeckViewModel().getState() == DeckViewModel.State.BEFORE_CREATE){
            fab.setVisibility(View.GONE);
        }
        else{
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.getCardViewModel().setCard(new Card());
                activity.getCardViewModel().setState(CardViewModel.State.BEFORE_CREATE);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_cardListFragment_to_editCardFragment);
            }
        });
        if(deck.getTitle() != null){
            EditText titleEditText = (EditText) view.findViewById(R.id.titleEditText);
            titleEditText.setText(deck.getTitle());
        }
        if(deck.getSubject() != null){
            spin.setSelection(deck.getSubject().ordinal());
        }
        if(deck.getDueDate() != null) {
            TextView dueDateTextView = (TextView) view.findViewById(R.id.dueDateTextView);
            dueDateTextView.setText(deck.getDueDate().toString());
        }

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(deck.getTitle() == null || deck.getTitle().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("Title cannot be empty");
                    alertDialogBuilder.setMessage("No new deck will be created");
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
                if(activity.getDeckViewModel().getState() == DeckViewModel.State.BEFORE_CREATE) {
                    activity.getDeckViewModel().setState(DeckViewModel.State.CREATED);
                    message = "Deck created successfully";
                }
                else if (activity.getDeckViewModel().getState() == DeckViewModel.State.BEFORE_EDIT){
                    activity.getDeckViewModel().setState(DeckViewModel.State.EDITED);
                    message = "Deck edited successfully";
                }
                activity.getDeckViewModel().setUpdatedDeck(deck);
                activity.getDeckViewModel().notifyChange();
                NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                controller.popBackStack();


                // check if due date is past current date and check if it is null
                System.out.println(dateChanged);
                if( deck.getDueDate() != null && deck.getDueDate().getTime() > new Date().getTime() && dateChanged){

                    title = activity.findViewById(R.id.titleEditText);
                    description = activity.findViewById(R.id.subjectSpinner);
                    date = activity.findViewById(R.id.dueDateTextView);
                    calendarSwitch = activity.findViewById(R.id.calendarEventSwitch);

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setTitle("Select your answer.");
                    builder.setMessage("Would you like to set a calendar date?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(!title.getText().toString().isEmpty() && !date.getText().toString().isEmpty()){
                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                intent.setData(CalendarContract.Events.CONTENT_URI);
                                intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, deck.getDueDate().getTime());
                                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, deck.getDueDate().getTime()+10);
                                intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getSelectedItem().toString() + " cards");


                                activity.startActivity(intent);
                            }
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context,"No Button Clicked",Toast.LENGTH_SHORT).show();
                        }
                    });

                    System.out.println(calendarSwitch);
                    System.out.println(activity.findViewById(R.id.calendarEventSwitch));
                    System.out.println(activity);
                    activity.findViewById(R.id.calendarEventSwitch);
                    if(calendarSwitch.isChecked()){
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                    // override run so it does the notification once its past the due date
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(deck.getDueDate().getTime() - MILLIS_IN_A_DAY > new Date().getTime()){
                                    Long sleepTime = (deck.getDueDate().getTime() - MILLIS_IN_A_DAY) - new Date().getTime();
                                    Thread.sleep(sleepTime);
                                    dayBeforeNotification();
                                }
                                Thread.sleep(deck.getDueDate().getTime() - new Date().getTime());
                                overdueNotification();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
                Snackbar snackbar = Snackbar
                        .make(activity.getBinding().getRoot(), message, 2000);

                snackbar.show();

            }
        });

        EditText deckTitleEditText = view.findViewById(R.id.titleEditText);
        deckTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                deck.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
        dateChanged = true;


        DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.create(date, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // set date to what user picked

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 59);
                Date date = calendar.getTime();


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
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        activity = (MainActivity)getActivity();
        dateChanged = false;
        CardTable cardTable = (CardTable) activity.getCardDBHandler().getCardTable();
        activity.getCardViewModel().addOnUpdateListener(this, new ObservableModel.OnUpdateListener<CardViewModel>() {
            @Override
            public void onUpdate(CardViewModel item){
                switch (item.getState()) {
                    case EDITED:
                        adapter.setCard(item.getUpdatedCard());
                        adapter.notifyDataSetChanged();
                        try {
                            activity.getCardDBHandler().getCardTable().update(item.getUpdatedCard());
                            activity.showSnackbar("Card edited successfully!");
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case CREATED:
                        if(deck != null){
                            deck.getCards().add(item.getUpdatedCard());
                        }
                        adapter.notifyDataSetChanged();
                        try {
                            activity.getCardDBHandler().getCardTable().create(item.getUpdatedCard());
                            activity.showSnackbar("Card created successfully!");
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }

                        break;
                    case BEFORE_EDIT:
                    case BEFORE_CREATE:
                    case NONE:
                        // do nothing
                }

                // TODO: maybe? item.setState(TasksViewModel.State.NONE);

            }
        });
    }
}