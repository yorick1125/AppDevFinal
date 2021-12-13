package com.example.flashcardapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.animation.AnimatorSet;

import android.os.Bundle;

import com.example.flashcardapplication.model.CardDBHandler;
import com.example.flashcardapplication.model.DeckDBHandler;
import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.viewmodel.DeckViewModel;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flashcardapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {


    public static String DECK_OVERDUE_NOTIFICATION_CHANNEL = "deck-overdue-notification-channel";
    public static String DECK_DUE_IN_DAY_NOTIFICATION_CHANNEL = "deck-due-in-day-notification-channel";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DeckDBHandler deckDBHandler;
    private CardDBHandler cardDBHandler;

    public DeckDBHandler getDeckDBHandler(){
        return deckDBHandler;
    }
    public CardDBHandler getCardDBHandler() { return cardDBHandler; }
    private DeckViewModel deckViewModel;
    private CardViewModel cardViewModel;


    public MainActivity()
    {
        deckViewModel = new DeckViewModel();
    }

    public DeckViewModel getDeckViewModel() {
        return deckViewModel;
    }
    public CardViewModel getCardViewModel() {
        return cardViewModel;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create Notification Channel
        String name1 = "Decks Overdue";
        String name2 = "Decks Due In A Day";
        String description1 = "Notifications about decks overdue";
        String description2 = "Notifications about decks due in a day";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channelOverdue = new NotificationChannel(DECK_OVERDUE_NOTIFICATION_CHANNEL, name1, importance);
        channelOverdue.setDescription(description1);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManagerOverdue = getSystemService(NotificationManager.class);
        notificationManagerOverdue.createNotificationChannel(channelOverdue);

        NotificationChannel channelDueInDay = new NotificationChannel(DECK_DUE_IN_DAY_NOTIFICATION_CHANNEL, name2, importance);
        channelDueInDay.setDescription(description2);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManagerDueInDay = getSystemService(NotificationManager.class);
        notificationManagerDueInDay.createNotificationChannel(channelDueInDay);

        deckDBHandler = new DeckDBHandler(this);
        cardDBHandler = new CardDBHandler(this);
        deckViewModel = new DeckViewModel();
        cardViewModel = new CardViewModel();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}