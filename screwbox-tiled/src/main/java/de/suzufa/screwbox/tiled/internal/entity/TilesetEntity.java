package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.utils.Resources;

public class TilesetEntity {// cannot be replaced by record: not all properties are final

    private String backgroundcolor;
    private int columns;
    private int firstgid;
    private GridEntity grid;
    private String image;
    private int imageheight;
    private int imagewidth;
    private int margin;
    private String name;
    private String objectalignment;
    private List<PropertyEntity> properties = new ArrayList<>();
    private String source;
    private int spacing;
    private List<TerrainEntity> terrains = new ArrayList<>();
    private int tilecount;
    private String tiledversion;
    private int tileheight;
    private TileOffsetEntity tileoffset;
    private List<TileEntity> tiles = new ArrayList<>();
    private int tilewidth;
    private TransformationEntity transformations;
    private String transparentcolor;
    private String type;
    private double version;

    public static TilesetEntity load(final String fileName) {
        final TilesetEntity tilesetEntity = Resources.loadJson(fileName, TilesetEntity.class);
        final String fileNameWithoudDirectories = fileName.split("/")[fileName.split("/").length - 1];
        final String correctPath = fileName.replace(fileNameWithoudDirectories, tilesetEntity.getImage());
        tilesetEntity.setImage(correctPath);
        return tilesetEntity;
    }

    public String getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getFirstgid() {
        return firstgid;
    }

    public void setFirstgid(int firstgid) {
        this.firstgid = firstgid;
    }

    public GridEntity getGrid() {
        return grid;
    }

    public void setGrid(GridEntity grid) {
        this.grid = grid;
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

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectalignment() {
        return objectalignment;
    }

    public void setObjectalignment(String objectalignment) {
        this.objectalignment = objectalignment;
    }

    public List<PropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public List<TerrainEntity> getTerrains() {
        return terrains;
    }

    public void setTerrains(List<TerrainEntity> terrains) {
        this.terrains = terrains;
    }

    public int getTilecount() {
        return tilecount;
    }

    public void setTilecount(int tilecount) {
        this.tilecount = tilecount;
    }

    public String getTiledversion() {
        return tiledversion;
    }

    public void setTiledversion(String tiledversion) {
        this.tiledversion = tiledversion;
    }

    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    public TileOffsetEntity getTileoffset() {
        return tileoffset;
    }

    public void setTileoffset(TileOffsetEntity tileoffset) {
        this.tileoffset = tileoffset;
    }

    public List<TileEntity> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileEntity> tiles) {
        this.tiles = tiles;
    }

    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    public TransformationEntity getTransformations() {
        return transformations;
    }

    public void setTransformations(TransformationEntity transformations) {
        this.transformations = transformations;
    }

    public String getTransparentcolor() {
        return transparentcolor;
    }

    public void setTransparentcolor(String transparentcolor) {
        this.transparentcolor = transparentcolor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

}
