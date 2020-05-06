package com.matthewcannefax.pokemonbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.matthewcannefax.pokemonbook.model.Pokemon;

import java.util.List;

@Dao
public interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Pokemon pokemon);

    @Query("DELETE FROM pokemon_table")
    void deleteAll();

    @Query("SELECT * FROM pokemon_table ORDER BY pokemon_id ASC")
    LiveData<List<Pokemon>> getAllPokemonOrderedByID();

    @Query("SELECT * FROM pokemon_table WHERE pokemon_id =:id")
    Pokemon getPokemonById(int id);

}
