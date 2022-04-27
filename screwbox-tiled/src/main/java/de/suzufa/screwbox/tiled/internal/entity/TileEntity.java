package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

public class TileEntity {

    private List<FrameEntity> animation = new ArrayList<>();
    private int id;
    private String image;

    private int imageheight;
    private int imagewidth;
    private LayerEntity objectgroup;
    private double probability;
    private List<PropertyEntity> properties = new ArrayList<>();
    private List<Integer> terrain;
    private String type;

    public List<FrameEntity> getAnimation() {
        return animation;
    }

    public void setAnimation(List<FrameEntity> animation) {
        this.animation = animation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageheight() {
        return imageheight;
    }

    public void setImageheight(int imageheight) {
        this.imageheight = imageheight;
    }

    public int getImagewidth() {
        return imagewidth;
    }

    public void setImagewidth(int imagewidth) {
        this.imagewidth = imagewidth;
    }

    public LayerEntity getObjectgroup() {
        return objectgroup;
    }

    public void setObjectgroup(LayerEntity objectgroup) {
        this.objectgroup = objectgroup;
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

    public List<Integer> getTerrain() {
        return terrain;
    }

    public void setTerrain(List<Integer> terrain) {
        this.terrain = terrain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
