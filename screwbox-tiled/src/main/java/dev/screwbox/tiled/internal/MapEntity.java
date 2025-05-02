package dev.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

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

    public static MapEntity load(final String fileName) {
        final MapEntity map = JsonLoader.loadJson(fileName, MapEntity.class);
        final String directory = getDirectory(fileName);
        map.embedExternalTilesets(directory);
        map.embedObjectTemplates(directory);
        return map;
    }

    private static String getDirectory(final String fileName) {
        final String[] parts = fileName.split("/");
        final String file = parts[parts.length - 1];
        return fileName.replace(file, "");
    }

    private void embedObjectTemplates(final String directory) {
        for (final LayerEntity layer : getLayers()) {
            for (int i = 0; i < layer.objects().size(); i++) {
                final ObjectEntity object = layer.objects().get(i);
                if (nonNull(object.getTemplate())) {
                    final var replacement = JsonLoader.loadJson(
                            directory + object.getTemplate(),
                            ObjectTemplateEntity.class)
                            .object();
                    replacement.setId(object.getId());
                    replacement.setX(object.getX());
                    replacement.setY(object.getY());
                    if (object.getWidth() != 0 && object.getHeight() != 0) {
                        replacement.setWidth(object.getWidth());
                        replacement.setHeight(object.getHeight());
                    }

                    replacement.setProperties(object.getProperties());
                    layer.objects().set(i, replacement);
                }
            }
        }
    }

    private void embedExternalTilesets(final String directory) {
        final List<TilesetEntity> fullTilesets = new ArrayList<>();
        for (final TilesetEntity tileset : getTilesets()) {
            if (tileset.getSource() == null) {
                fullTilesets.add(tileset);
            } else {
                final TilesetEntity externalTileset = TilesetEntity.load(directory + tileset.getSource());
                externalTileset.setFirstgid(tileset.getFirstgid());
                fullTilesets.add(externalTileset);
            }
        }
        setTilesets(fullTilesets);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<PropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyEntity> properties) {
        this.properties = properties;
    }

    public int getCompressionlevel() {
        return compressionlevel;
    }

    public void setCompressionlevel(int compressionlevel) {
        this.compressionlevel = compressionlevel;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHexsidelength() {
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

    public int getNextlayerid() {
        return nextlayerid;
    }

    public void setNextlayerid(int nextlayerid) {
        this.nextlayerid = nextlayerid;
    }

    public int getNextobjectid() {
        return nextobjectid;
    }

    public void setNextobjectid(int nextobjectid) {
        this.nextobjectid = nextobjectid;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getRenderorder() {
        return renderorder;
    }

    public void setRenderorder(String renderorder) {
        this.renderorder = renderorder;
    }

    public String getStaggeraxis() {
        return staggeraxis;
    }

    public void setStaggeraxis(String staggeraxis) {
        this.staggeraxis = staggeraxis;
    }

    public String getStaggerindex() {
        return staggerindex;
    }

    public void setStaggerindex(String staggerindex) {
        this.staggerindex = staggerindex;
    }

    public String getTiledversion() {
        return tiledversion;
    }

    public void setTiledversion(String tiledversion) {
        this.tiledversion = tiledversion;
    }

    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<LayerEntity> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerEntity> layers) {
        this.layers = layers;
    }

    public List<TilesetEntity> getTilesets() {
        return tilesets;
    }

    public void setTilesets(List<TilesetEntity> tilesets) {
        this.tilesets = tilesets;
    }

}
