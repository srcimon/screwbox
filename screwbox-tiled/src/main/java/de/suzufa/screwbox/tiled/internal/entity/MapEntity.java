package de.suzufa.screwbox.tiled.internal.entity;

import java.util.List;

public class MapEntity { // cannot be replaced by record: tilesets are not final

    private String backgroundColor;
    private List<PropertyEntity> properties;
    private int compressionlevel;
    private int height;
    private int hexsidelength;
    private boolean infinite;
    private int nextlayerid;
    private int nextobjectid;
    private String orientation;
    private String renderorder;
    private String staggeraxis;
    private String staggerindex;
    private String tiledversion;
    private int tilewidth;
    private int tileheight;
    private String type;
    private float version;
    private int width;
    private List<LayerEntity> layers;
    private List<TilesetEntity> tilesets;

    public String backgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<PropertyEntity> properties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public int compressionlevel() {
        return compressionlevel;
    }

    public void setCompressionlevel(int compressionlevel) {
        this.compressionlevel = compressionlevel;
    }

    public int height() {
        return height;
    }

    public void height(int height) {
        this.height = height;
    }

    public int hexsidelength() {
        return hexsidelength;
    }

    public void setHexsidelength(int hexsidelength) {
        this.hexsidelength = hexsidelength;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public int nextlayerid() {
        return nextlayerid;
    }

    public void setNextlayerid(int nextlayerid) {
        this.nextlayerid = nextlayerid;
    }

    public int nextobjectid() {
        return nextobjectid;
    }

    public void setNextobjectid(int nextobjectid) {
        this.nextobjectid = nextobjectid;
    }

    public String orientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String renderorder() {
        return renderorder;
    }

    public void setRenderorder(String renderorder) {
        this.renderorder = renderorder;
    }

    public String staggeraxis() {
        return staggeraxis;
    }

    public void setStaggeraxis(String staggeraxis) {
        this.staggeraxis = staggeraxis;
    }

    public String staggerindex() {
        return staggerindex;
    }

    public void setStaggerindex(String staggerindex) {
        this.staggerindex = staggerindex;
    }

    public String tiledversion() {
        return tiledversion;
    }

    public void setTiledversion(String tiledversion) {
        this.tiledversion = tiledversion;
    }

    public int tilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    public int tileheight() {
        return tileheight;
    }

    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float version() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public int width() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<LayerEntity> layers() {
        return layers;
    }

    public void setLayers(List<LayerEntity> layers) {
        this.layers = layers;
    }

    public List<TilesetEntity> tilesets() {
        return tilesets;
    }

    public void setTilesets(List<TilesetEntity> tilesets) {
        this.tilesets = tilesets;
    }

}
