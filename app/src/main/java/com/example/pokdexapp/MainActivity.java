package com.example.pokdexapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText pokemonSearch;
    private ImageView pokemonSprite;
    private TextView pokemonName;
    private TextView pokemonNo;
    private ImageView pokemonType1;
    private ImageView pokemonType2;
    private TextView pokemonDescription;
    private ImageButton searchButton;
    private ImageButton backButton;
    private ImageButton forwardButton;
    public int currentPokemonNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonSearch = findViewById(R.id.pokemonSearch);
        pokemonSprite = findViewById(R.id.pokemonSprite);
        pokemonName = findViewById(R.id.pokemonName);
        pokemonNo = findViewById(R.id.pokemonNo);
        pokemonType1 = findViewById(R.id.pokemonType1);
        pokemonType2 = findViewById(R.id.pokemonType2);
        pokemonDescription = findViewById(R.id.pokemonDescription);
        searchButton = findViewById(R.id.searchButton);
        backButton = findViewById(R.id.backButton);
        forwardButton = findViewById(R.id.forwardButton);
        fetchPokemonInfo(String.valueOf(currentPokemonNo), pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                pokemonType2, pokemonDescription);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pokemon = pokemonSearch.getText().toString().trim().toLowerCase();
                if (!pokemon.isEmpty()) {
                    fetchPokemonInfo(pokemon, pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                            pokemonType2, pokemonDescription);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid Pokémon name.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPokemonNo == 1) {
                    fetchPokemonInfo(String.valueOf(currentPokemonNo), pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                            pokemonType2, pokemonDescription);
                } else {
                    currentPokemonNo -= 1;
                    fetchPokemonInfo(String.valueOf(currentPokemonNo), pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                            pokemonType2, pokemonDescription);
                }
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPokemonNo == 1008) {
                    fetchPokemonInfo(String.valueOf(currentPokemonNo), pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                            pokemonType2, pokemonDescription);
                } else {
                    currentPokemonNo += 1;
                    fetchPokemonInfo(String.valueOf(currentPokemonNo), pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                            pokemonType2, pokemonDescription);
                }
            }
        });
    }

    private void fetchPokemonInfo(String pokemon, ImageView pokemonSprite, TextView pokemonName, TextView pokemonNo,
                                  ImageView pokemonType1, ImageView pokemonType2, TextView pokemonDescription) {
        new FetchPokemonInfoTask(pokemon, pokemonSprite, pokemonName, pokemonNo, pokemonType1,
                pokemonType2, pokemonDescription, this).execute();
    }
}