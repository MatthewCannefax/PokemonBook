package com.matthewcannefax.pokemonbook.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Entity(tableName = "pokemon_table")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {

    public Pokemon(){}

    public static Pokemon getPokemon(){
        return new Pokemon();
    }
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pokemon_id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @Ignore
    @JsonProperty("types")
    private List<PokemonTypeWrapper> type;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "information")
    private String information;

    @Ignore
    @JsonProperty("sprites")
    private Sprite sprite;

    @ColumnInfo(name = "sprite_url")
    private String spriteUrl;

    @ColumnInfo(name = "type_string")
    private String typeString;

    @Ignore
    private Drawable drawable;

    @ColumnInfo(name = "height")
    @JsonProperty("height")
    private Integer height;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "generation")
    private int generation;

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getSpriteUrl() {
        return sprite.getUrl();
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
        if(sprite == null){
            sprite = new Sprite();
        }
        this.sprite.setUrl(spriteUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public void setTypeString(String typeString){this.typeString = typeString;}

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
        return String.format("%s %s", id, name);
    }
}
