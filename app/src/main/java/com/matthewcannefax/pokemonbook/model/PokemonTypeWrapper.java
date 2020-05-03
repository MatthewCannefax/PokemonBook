package com.matthewcannefax.pokemonbook.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonTypeWrapper {

    public PokemonTypeWrapper(){}

    @JsonProperty("type")
    private PokemonType type;

    @JsonProperty("slot")
    private int slot;

    public PokemonType getType() {
        return type;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @NonNull
    @Override
    public String toString() {
        return type.getName();
    }
}
