package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

public class TerrainEntity {

    private String name;
    private List<PropertyEntity> properties = new ArrayList<>();
    private int tile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
