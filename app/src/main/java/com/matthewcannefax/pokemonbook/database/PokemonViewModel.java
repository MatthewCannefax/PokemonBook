package com.matthewcannefax.pokemonbook.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.matthewcannefax.pokemonbook.model.Pokemon;

import java.util.List;

public class PokemonViewModel extends AndroidViewModel {

    private PokemonRepo mRepo;

    private LiveData<List<Pokemon>> mAllPokemon;

    public PokemonViewModel (Application application){
        super(application);
        mRepo = new PokemonRepo(application);
        mAllPokemon = mRepo.getmAllPokemon();
    }

    public LiveData<List<Pokemon>> getmAllPokemon() {return mAllPokemon;};

    public void insert(Pokemon pokemon){mRepo.insert(pokemon);}

    public Pokemon getPokemonById(int id){
        return mRepo.getPokemonById(id);
    }

    public void deleteAll(){mRepo.deleteAll();}
}
