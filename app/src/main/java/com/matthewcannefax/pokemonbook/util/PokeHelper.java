package com.matthewcannefax.pokemonbook.util;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;

public class PokeHelper {



    public static void main(String[] args){
        PokeApi pokeApi = new PokeApiClient();
        PokemonSpecies pokemon = pokeApi.getPokemonSpecies(2);
        System.out.println(pokemon);
    }

}
