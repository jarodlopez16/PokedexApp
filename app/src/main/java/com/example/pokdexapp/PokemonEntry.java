package com.example.pokdexapp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class PokemonEntry {
    private String description;
    private Integer pokemonNumber;
    public static PokemonEntry fromJson(JSONObject jsonResponse) {
        try {
            PokemonEntry pokemonEntry = new PokemonEntry();
            JSONArray descriptionsArray = jsonResponse.getJSONArray("flavor_text_entries");
            Log.i("PokemonEntry", "Got descriptions array");
            JSONObject descriptionObject = descriptionsArray.getJSONObject(0);
            Log.i("PokemonEntry", "Got descriptions object");
            pokemonEntry.description = descriptionObject.getString("flavor_text");
            Log.i("PokemonEntry", "Got description");
            pokemonEntry.pokemonNumber = jsonResponse.getInt("id");
            Log.i("PokemonEntry", "Got id");
            return pokemonEntry;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDescription() {
        return description;
    }
    public Integer getPokemonNumber() {
        return pokemonNumber;
    }
}
