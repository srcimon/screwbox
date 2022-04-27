package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

public class WangColorEntity {

    private String color;
    private String name;
    private double probability;
    private List<PropertyEntity> properties = new ArrayList<>();
    private int tile;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public List<PropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public int getTile() {
        return tile;
    }

    public void setTile(int tile) {
        this.tile = tile;
    }

}
