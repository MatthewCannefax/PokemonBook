package com.matthewcannefax.pokemonbook.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.matthewcannefax.pokemonbook.model.Pokemon;

import java.util.List;

public class PokemonRepo {
    private PokemonDao mPokemonDao;
    private LiveData<List<Pokemon>> mAllPokemon;

    PokemonRepo(Application application){
        PokemonRoomDatabase db = PokemonRoomDatabase.getDatabase(application);
        mPokemonDao = db.pokemonDao();
        mAllPokemon = mPokemonDao.getAllPokemonOrderedByID();
    }

    LiveData<List<Pokemon>> getmAllPokemon(){
        return mAllPokemon;
    }

    void insert(Pokemon pokemon){
        PokemonRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPokemonDao.insert(pokemon);
        });
    }

    public Pokemon getPokemonById(int id){
        return mPokemonDao.getPokemonById(id);
    }

    void deleteAll(){
        PokemonRoomDatabase.databaseWriteExecutor.execute(() ->{
            mPokemonDao.deleteAll();
        });
    }
}
