package de.suzufa.screwbox.tiled.internal.entity;

public class ObjectTemplateEntity {

    private String type;
    private TilesetEntity tileset;
    private ObjectEntity object;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TilesetEntity getTileset() {
        return tileset;
    }

    public void setTileset(TilesetEntity tileset) {
        this.tileset = tileset;
    }

    public ObjectEntity getObject() {
        return object;
    }

    public void setObject(ObjectEntity object) {
        this.object = object;
    }

}
