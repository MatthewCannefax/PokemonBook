package com.matthewcannefax.pokemonbook.model;

public class PokemonReference {
    public PokemonReference(){}

    public PokemonReference(int id, String name){
        this.id = id;
        setName(name);
    }

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
        String newName = name.replaceFirst(
                name.substring(0,1),
                name.substring(0, 1).toUpperCase()
        );
        this.name = newName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
