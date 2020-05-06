package com.matthewcannefax.pokemonbook.util;

import android.graphics.drawable.Drawable;

import com.matthewcannefax.pokemonbook.model.Pokemon;
import com.matthewcannefax.pokemonbook.model.PokemonReference;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Genus;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;

public class PokeHelper {
    public static final String GET_POKEMON_BY_ID_URL = "https://pokeapi.co/api/v2/pokemon/%s";


    public static Pokemon getPokemonById(int id){
        try {
            //using the spring resttemplate to call from the api
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            //get the initial pokemon data from the api
            Pokemon pokemon = restTemplate.getForObject(String.format(GET_POKEMON_BY_ID_URL, id), Pokemon.class);

            //there are some things that the first api call does not pick up, so I use the pokeapi sdk to call in the remaining data that I want
            PokeApi pokeApi = new PokeApiClient();
            PokemonSpecies species = pokeApi.getPokemonSpecies(id);

            //set the name from the sdk
            pokemon.setName(species.getName());

            //set the id from sdk
            pokemon.setId(species.getId());

            //loop through genera and find the english version, then break the loop
            for(Genus genus : species.getGenera()){
                if(genus.getLanguage().getName().equals("en")){
                    pokemon.setDescription(genus.getGenus());
                    break;
                }
            }

            //loop through the flavor text and find the english version
            for(PokemonSpeciesFlavorText flavorText : species.getFlavorTextEntries()){
                if(flavorText.getLanguage().getName().equals("en")){
                    String newFlavor = flavorText.getFlavorText().replace("\n", " ");
                    pokemon.setInformation(newFlavor);
                    break;
                }
            }

            //make sure the sprite url is on null and set it
            if(pokemon.getSprite().getUrl() != null){
                InputStream inputStream = (InputStream) new URL(pokemon.getSprite().getUrl()).getContent();
                Drawable d = Drawable.createFromStream(inputStream, "srcName");
                pokemon.setDrawable(d);
            }
            return pokemon;
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static List<PokemonReference> getListOfPokemon(){
        try{
            PokeApi pokeApi = new PokeApiClient();
            List<NamedApiResource> resourceList = pokeApi.getPokemonSpeciesList(0, 807).getResults();
            List<PokemonReference> pokemonReferences = new ArrayList<>();

            for(NamedApiResource resource : resourceList){
                PokemonReference reference = new PokemonReference();
                reference.setId(resource.getId());
                reference.setName(resource.getName());
                pokemonReferences.add(reference);
            }

            return pokemonReferences;
        }catch (Exception e){
            return null;
        }
    }

}
