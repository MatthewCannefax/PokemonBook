package com.matthewcannefax.pokemonbook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewcannefax.pokemonbook.R;
import com.matthewcannefax.pokemonbook.model.Pokemon;
import com.matthewcannefax.pokemonbook.model.PokemonReference;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
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

    private Button apiButton;
    private EditText etPokeName;

    private ImageView ivPokemonSprite;

    private TextView tvPokemonName;
    private TextView tvPokemonDescription;
    private TextView tvPokemonType;
    private TextView tvPokemonInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        apiButton = findViewById(R.id.getApiCallBTN);
        etPokeName = findViewById(R.id.etPokeName);

        ivPokemonSprite = findViewById(R.id.ivPokemonSprite);

        tvPokemonName = findViewById(R.id.tvPokemonName);
        tvPokemonDescription = findViewById(R.id.tvPokemonDescription);
        tvPokemonType = findViewById(R.id.tvPokemonType);
        tvPokemonInformation = findViewById(R.id.tvPokemonInformation);


        apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int id = Integer.parseInt(etPokeName.getText().toString());
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
        });
    }

    //Probably will be moving this into a different class at some point
    //this class is execute on click of the button
    //eventually it will be execute on the selection of a drop down list
    //after the DB is created it will check the db first before calling the api
    private class HttpReqTask extends AsyncTask<Void, Void, Pokemon>{

        @Override
        protected Pokemon doInBackground(Void... voids) {

            try {
                String apiUrl = String.format("https://pokeapi.co/api/v2/pokemon/%s", etPokeName.getText().toString().toLowerCase());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Pokemon pokemon = restTemplate.getForObject(apiUrl, Pokemon.class);

                PokeApi pokeApi = new PokeApiClient();
                PokemonSpecies species = pokeApi.getPokemonSpecies(Integer.parseInt(etPokeName.getText().toString()));

                pokemon.setName(species.getName());

                //find the genus(description) that we want
                for(Genus genus : species.getGenera()){
                    if(genus.getLanguage().getName().equals("en")){
                        pokemon.setDescription(genus.getGenus());
                        break;
                    }
                }

                //find the flavorText we want(information)
                for(PokemonSpeciesFlavorText flavorText : species.getFlavorTextEntries()){
                    if(flavorText.getLanguage().getName().equals("en")){
                        String newFlavor = flavorText.getFlavorText().replace("\n", " ");
                        pokemon.setInformation(flavorText.getFlavorText());
                        break;
                    }
                }

                if (pokemon.getSprite().getUrl() != null) {
                    InputStream inputStream = (InputStream) new URL(pokemon.getSprite().getUrl()).getContent();
                    Drawable d = Drawable.createFromStream(inputStream, "srcName");
                    pokemon.setDrawable(d);
                }
                return pokemon;
            }catch (Exception ex){
                Log.e("EPIC_FAIL", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            super.onPostExecute(pokemon);
            //after the thread is finished load the data into the activity
            //this is where it will load into the DB as well
            tvPokemonName.setText(pokemon.getName());
            tvPokemonDescription.setText(pokemon.getDescription());
            tvPokemonInformation.setText(pokemon.getInformation());
            tvPokemonType.setText(pokemon.getTypeString());
            ivPokemonSprite.setImageDrawable(pokemon.getDrawable());
        }
    }

    private class fillComboBox extends AsyncTask<Void, Void, List<PokemonReference>> {

        @Override
        protected List<PokemonReference> doInBackground(Void... voids) {
            PokeApi pokeApi = new PokeApiClient();
            NamedApiResourceList list = pokeApi.getPokemonSpeciesList(0, 807);//use this to populate a dropdown box

            return null;

        }

        @Override
        protected void onPostExecute(List<PokemonReference> pokemonReferences) {
            super.onPostExecute(pokemonReferences);


        }
    }


}