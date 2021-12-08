package com.example.flashcardapplication;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.icu.number.Scale;
import android.os.Bundle;

import com.example.flashcardapplication.model.CardDBHandler;
import com.example.flashcardapplication.model.DeckDBHandler;
import com.example.flashcardapplication.viewmodel.CardViewModel;
import com.example.flashcardapplication.viewmodel.DeckViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flashcardapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

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
    private AnimatorSet frontAnimation;
    private AnimatorSet backAnimation;

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
        deckDBHandler = new DeckDBHandler(this);
        cardDBHandler = new CardDBHandler(this);
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