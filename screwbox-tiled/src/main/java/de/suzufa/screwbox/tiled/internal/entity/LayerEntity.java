package de.suzufa.screwbox.tiled.internal.entity;

import java.util.ArrayList;
import java.util.List;

public class LayerEntity {

    private List<ChunkEntity> chunks = new ArrayList<>();
    private String compression;
    private List<Integer> data;
    private String draworder;
    private String encoding;
    private int height;
    private int id;
    private String image;
    private double parallaxx;
    private double parallaxy;
    private List<LayerEntity> layers;
    private String name;
    private List<ObjectEntity> objects = new ArrayList<>();
    private double offsetx;
    private double offsety;
    private double opacity;
    private List<PropertyEntity> properties;
    private int startx;
    private int starty;
    private String tintcolor;
    private String transparentcolor;
    private String type;
    private boolean visible;
    private int width;
    private int x;
    private int y;

    public List<ChunkEntity> getChunks() {
        return chunks;
    }

    public void setChunks(List<ChunkEntity> chunks) {
        this.chunks = chunks;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getDraworder() {
        return draworder;
    }

    public void setDraworder(String draworder) {
        this.draworder = draworder;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public double getParallaxx() {
        return parallaxx;
    }

    public void setParallaxx(double parallaxx) {
        this.parallaxx = parallaxx;
    }

    public double getParallaxy() {
        return parallaxy;
    }

    public void setParallaxy(double parallaxy) {
        this.parallaxy = parallaxy;
    }

    public List<LayerEntity> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerEntity> layers) {
        this.layers = layers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectEntity> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectEntity> objects) {
        this.objects = objects;
    }

    public double getOffsetx() {
        return offsetx;
    }

    public void setOffsetx(double offsetx) {
        this.offsetx = offsetx;
    }

    public double getOffsety() {
        return offsety;
    }

    public void setOffsety(double offsety) {
        this.offsety = offsety;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public List<PropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public int getStartx() {
        return startx;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public int getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public String getTintcolor() {
        return tintcolor;
    }

    public void setTintcolor(String tintcolor) {
        this.tintcolor = tintcolor;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
