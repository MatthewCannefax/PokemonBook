package com.matthewcannefax.pokemonbook.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    private Pokemon() {
    }

    public static Pokemon getPokemon(){
        return new Pokemon();
    }

  //  @JsonProperty("name")
    private String name;

    @JsonProperty("types")
    private List<PokemonTypeWrapper> type;

    private String description;

    private String information;

    @JsonProperty("sprites")
    private Sprite sprite;

    private String typeString;

    private Drawable drawable;

    @JsonProperty("height")
    private Integer height;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @JsonProperty("weight")
    private Integer weight;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getTypeString() {
        return typeString;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public List<PokemonTypeWrapper> getType() {
        return type;
    }

    public void setType(List<PokemonTypeWrapper> type) {
        this.type = type;
        StringBuilder builder = new StringBuilder();
        for(int i = type.size() - 1; i >= 0; i--){
            String s = type.get(i).getType().getName().substring(0, 1).toUpperCase();
            builder.append(type.get(i).getType().getName().replaceFirst(type.get(i).getType().getName().substring(0, 1), s));
            if(i > 0){
                builder.append("-");
            }
        }
        typeString = builder.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String s = name.substring(0, 1).toUpperCase();
        this.name = name.replaceFirst(name.substring(0, 1), s);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}