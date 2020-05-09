package com.matthewcannefax.pokemonbook.model;

public enum PokemonGen {
    ALL("All Pokemon", 0),
    FAVORITES("My Favorites", 1);

    private String name;
    private int position;

    public String getName(){
        return this.name;
    }

    public int getPosition(){
        return this.position;
    }

    PokemonGen(String name, int position){
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return name;
    }
}
