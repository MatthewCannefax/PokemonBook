package com.matthewcannefax.pokemonbook.model;

public class PokemonReference {
    public PokemonReference(){}

    private String name;

    private int id;

    @Override
    public String toString() {
        return "PokemonReference{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
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
