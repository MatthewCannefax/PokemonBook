package com.matthewcannefax.pokemonbook.model;

public class PokemonReference {
    public PokemonReference(){}

    private String name;

    private int id;

    @Override
    public String toString() {
        return String.format("%s %s", id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
