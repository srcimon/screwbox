package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

public class WangSetEntity {

    private List<WangColorEntity> colors = new ArrayList<>();
    private String name;
    private List<PropertyEntity> properties = new ArrayList<>();
    private int tile;
    private String type;
    private List<WangTileEntity> wangtiles = new ArrayList<>();

    public List<WangColorEntity> getColors() {
        return colors;
    }

    public void setColors(List<WangColorEntity> colors) {
        this.colors = colors;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<WangTileEntity> getWangtiles() {
        return wangtiles;
    }

    public void setWangtiles(List<WangTileEntity> wangtiles) {
        this.wangtiles = wangtiles;
    }

}
