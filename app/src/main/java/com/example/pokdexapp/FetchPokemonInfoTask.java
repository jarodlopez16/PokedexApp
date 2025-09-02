package com.example.pokdexapp;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchPokemonInfoTask extends AsyncTask<Void, Void, String> {
    private String pokemon;
    private ImageView pokemonSprite;
    private TextView pokemonName;
    private TextView pokemonNo;
    private ImageView pokemonType1;
    private ImageView pokemonType2;
    private TextView pokemonDescription;
    private MainActivity activity;
    public FetchPokemonInfoTask(String pokemon, ImageView pokemonSprite, TextView pokemonName, TextView pokemonNo,
                                ImageView pokemonType1, ImageView pokemonType2, TextView pokemonDescription, MainActivity activity) {
        this.pokemon = pokemon;
        this.pokemonSprite = pokemonSprite;
        this.pokemonName = pokemonName;
        this.pokemonNo = pokemonNo;
        this.pokemonType1 = pokemonType1;
        this.pokemonType2 = pokemonType2;
        this.pokemonDescription = pokemonDescription;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String apiUrl1 = "https://pokeapi.co/api/v2/pokemon/" + pokemon;
            URL url1 = new URL(apiUrl1);
            HttpURLConnection connection1 = (HttpURLConnection)url1.openConnection();
            connection1.setRequestMethod("GET");

            if (connection1.getResponseCode() != 200) {
                return "ERROR";
            }

            BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            StringBuilder response1 = new StringBuilder();
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                response1.append(line1);
            }
            reader1.close();

            JsonObject jsonResponse1 = JsonParser.parseString(response1.toString()).getAsJsonObject();

            JsonObject spritesObject = jsonResponse1.get("sprites").getAsJsonObject();
            String sprite = spritesObject.get("front_default").getAsString();

            String name = jsonResponse1.get("name").getAsString();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            String type1;
            String type2;
            JsonArray typesArray = jsonResponse1.get("types").getAsJsonArray();
            if (typesArray.size() > 1) {
                JsonObject type1Object = typesArray.get(0).getAsJsonObject();
                JsonObject type1Inner = type1Object.get("type").getAsJsonObject();
                type1 = type1Inner.get("name").getAsString();
                JsonObject type2Object = typesArray.get(1).getAsJsonObject();
                JsonObject type2Inner = type2Object.get("type").getAsJsonObject();
                type2 = type2Inner.get("name").getAsString();
            } else {
                JsonObject type1Object = typesArray.get(0).getAsJsonObject();
                JsonObject type1Inner = type1Object.get("type").getAsJsonObject();
                type1 = type1Inner.get("name").getAsString();
                type2 = "";
            }

            String apiUrl2 = "https://pokeapi.co/api/v2/pokemon-species/" + pokemon;
            URL url2 = new URL(apiUrl2);
            HttpURLConnection connection2 = (HttpURLConnection)url2.openConnection();
            connection2.setRequestMethod("GET");

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
            StringBuilder response2 = new StringBuilder();
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                response2.append(line2);
            }
            reader2.close();

            JsonObject jsonResponse2 = JsonParser.parseString(response2.toString()).getAsJsonObject();

            int pokedexNo = jsonResponse2.get("id").getAsInt();

            String description = null;
            JsonArray descriptionArray = jsonResponse2.get("flavor_text_entries").getAsJsonArray();
            for (int i = 0; i < descriptionArray.size(); i++) {
                JsonObject descriptionInner = descriptionArray.get(i).getAsJsonObject();
                JsonObject descLanguageObject = descriptionInner.get("language").getAsJsonObject();
                if (descLanguageObject.get("name").getAsString().equals("en")) {
                    description = descriptionInner.get("flavor_text").getAsString().replace("\n", " ");
                    break;
                }
            }

            return sprite + "<>" + name + "<>" + type1 + "<>" + type2 + "<>" + pokedexNo + "<>" + description;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching info for" + pokemon;
        }
    }

    @Override
    protected void onPostExecute(String pokemonInfo) {
        if (pokemonInfo.equals("ERROR")) {
            Toast.makeText(activity, "Please enter a valid Pokémon name.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] infoParts = pokemonInfo.split("<>");

        Glide.with(pokemonSprite.getContext())
                .load(infoParts[0])
                .into(pokemonSprite);

        pokemonName.setText(infoParts[1]);
        pokemonNo.setText("#" + infoParts[4]);

        int type1ID = pokemonType1.getContext().getResources().getIdentifier(infoParts[2], "drawable",
                pokemonType1.getContext().getPackageName());
        pokemonType1.setImageResource(type1ID);
        if (!infoParts[3].isEmpty()) {
            int type2ID = pokemonType2.getContext().getResources().getIdentifier(infoParts[3], "drawable",
                    pokemonType2.getContext().getPackageName());
            pokemonType2.setImageResource(type2ID);
        } else {
            pokemonType2.setImageDrawable(null);
        }

        activity.currentPokemonNo = Integer.parseInt(infoParts[4]);
        pokemonDescription.setText(infoParts[5]);
    }
}