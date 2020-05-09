package com.matthewcannefax.pokemonbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewcannefax.pokemonbook.R;
import com.matthewcannefax.pokemonbook.database.PokemonViewModel;
import com.matthewcannefax.pokemonbook.model.Pokemon;
import com.matthewcannefax.pokemonbook.model.PokemonGen;
import com.matthewcannefax.pokemonbook.model.PokemonReference;
import com.matthewcannefax.pokemonbook.util.PokeHelper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Genus;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;


public class MainActivity extends AppCompatActivity {

    Context mContext;

    private Spinner categorySpinner;
    private Spinner pokeSpinner;

    private ImageView ivPokemonFavorite;
    private ImageView ivPokemonSprite;

    private TextView tvPokemonName;
    private TextView tvPokemonDescription;
    private TextView tvPokemonType;
    private TextView tvPokemonInformation;

    private PokemonViewModel mPokemonViewModel;

    private Pokemon currentPokemon;

    //testing only
    private boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        categorySpinner = findViewById(R.id.categorySpinner);
        pokeSpinner = findViewById(R.id.pokeSpinner);

        ivPokemonFavorite = findViewById(R.id.ivPokemonFavorite);
        ivPokemonSprite = findViewById(R.id.ivPokemonSprite);

        tvPokemonName = findViewById(R.id.tvPokemonName);
        tvPokemonDescription = findViewById(R.id.tvPokemonDescription);
        tvPokemonType = findViewById(R.id.tvPokemonType);
        tvPokemonInformation = findViewById(R.id.tvPokemonInformation);

        mPokemonViewModel = new PokemonViewModel(getApplication());

        ArrayAdapter catAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, PokemonGen.values());
        categorySpinner.setAdapter(catAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PokemonGen gen = (PokemonGen)categorySpinner.getSelectedItem();
                new fillComboBox(gen.getPosition()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ivPokemonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPokemon.setFavorite(!currentPokemon.isFavorite());
                if(currentPokemon.isFavorite()){
                    ivPokemonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_star_filled));
                }else{
                    ivPokemonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_star_empty));
                }

                mPokemonViewModel.update(currentPokemon);
            }
        });

        pokeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    PokemonReference pokemonReference = (PokemonReference)pokeSpinner.getSelectedItem();
                    int id = pokemonReference.getId();
                    if (id <= 807 && id > 0) {
                        new HttpReqTask().execute();
                    } else {
                        throw new NumberFormatException();
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(mContext, "Please enter a valid number (1-807)", Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Probably will be moving this into a different class at some point
    //this class is execute on click of the button
    //eventually it will be execute on the selection of a drop down list
    //after the DB is created it will check the db first before calling the api
    private class HttpReqTask extends AsyncTask<Void, Void, Pokemon>{

        @Override
        protected Pokemon doInBackground(Void... voids) {

                PokemonReference pokemonReference = (PokemonReference)pokeSpinner.getSelectedItem();
                int id = pokemonReference.getId();
                Pokemon pokemon = mPokemonViewModel.getPokemonById(id);
                exists = false;
                if(pokemon != null){
                    exists = true;
                    if(pokemon.getDrawable() == null){
                        try {
                            PokeHelper.setPokemonDrawable(pokemon);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return pokemon;
                }
                return PokeHelper.getPokemonById(id);
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            super.onPostExecute(pokemon);

            currentPokemon = pokemon;

            if (!exists) {
                mPokemonViewModel.insert(pokemon);
            }
            //after the thread is finished load the data into the activity
            //this is where it will load into the DB as well
            tvPokemonName.setText(pokemon.getName());
            tvPokemonDescription.setText(pokemon.getDescription());
            tvPokemonInformation.setText(pokemon.getInformation());
            tvPokemonType.setText(pokemon.getTypeString());
            ivPokemonSprite.setImageDrawable(pokemon.getDrawable());

            if(pokemon.isFavorite()){
                ivPokemonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_star_filled));
            }else{
                ivPokemonFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_star_empty));
            }
        }
    }

    private class fillComboBox extends AsyncTask<Void, Void, List<PokemonReference>> {

        fillComboBox(){}

        fillComboBox(int categoryPosition){
            this.position = categoryPosition;
        }

        private int position = 0;

        @Override
        protected List<PokemonReference> doInBackground(Void... voids) {

            if(position == 0){
                return PokeHelper.getListOfPokemon();
            }else{
                List<PokemonReference> references = new ArrayList<>();
                List<Pokemon> pokemonList = mPokemonViewModel.getFavoritePokemon();
                for(Pokemon p : pokemonList){
                    references.add(new PokemonReference(p.getId(), p.getName()));
                }
                return references;
            }


        }

        @Override
        protected void onPostExecute(List<PokemonReference> pokemonReferences) {
            super.onPostExecute(pokemonReferences);

            pokeSpinner.setAdapter(new ArrayAdapter<PokemonReference>(mContext, R.layout.support_simple_spinner_dropdown_item, pokemonReferences));
        }
    }




}
