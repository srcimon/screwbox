package de.suzufa.screwbox.tiled;

import static java.util.Objects.isNull;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.internal.FrameEntity;
import de.suzufa.screwbox.tiled.internal.TileEntity;
import de.suzufa.screwbox.tiled.internal.TilesetEntity;

public class Tileset {

    private final Map<Integer, Sprite> spritesById = new HashMap<>();
    private final Map<String, Sprite> spritesByName = new HashMap<>();
    private final List<Sprite> allSprites = new ArrayList<>();

    public static Tileset fromJson(final String fileName) {
        final var tilesetEntity = TilesetEntity.load(fileName);
        return new Tileset(tilesetEntity);
    }

    Tileset() {
    }

    Tileset(final TilesetEntity tilesetEntity) {
        this(List.of(tilesetEntity));
    }

    Tileset(final List<TilesetEntity> tilesetEntities) {
        for (final TilesetEntity tilesetEntity : tilesetEntities) {
            addTilesToTileset(tilesetEntity);
        }
    }

    public void addTilesToTileset(final TilesetEntity tilesetEntity) {
        final String imageFileName = tilesetEntity.getImage();
        final Frame frame = Frame.fromFile(imageFileName);
        int localId = 0;

        // Read static images
        for (int y = 0; y < tilesetEntity.getImageheight(); y += tilesetEntity.getTileheight()) {
            for (int x = 0; x < tilesetEntity.getImagewidth(); x += tilesetEntity.getTilewidth()) {
                final Offset imageOffset = Offset.at(x, y);
                final Dimension imageSize = Dimension.of(tilesetEntity.getTilewidth(), tilesetEntity.getTileheight());
                final Frame subFrame = frame.subFrame(imageOffset, imageSize);
                final Sprite sprite = new Sprite(subFrame);
                addSprite(tilesetEntity.getFirstgid() + localId, sprite);
                localId++;
            }
        }

        for (final TileEntity tileEntity : tilesetEntity.getTiles()) {
            // read animated images and properties
            final List<Frame> frames = new ArrayList<>();
            for (final FrameEntity frameEntity : tileEntity.animation()) {
                final Image currentImage = findById(tilesetEntity.getFirstgid() + frameEntity.tileid()).singleFrame()
                        .image();
                frames.add(new Frame(currentImage, Duration.ofMillis(frameEntity.duration())));
            }
            final Sprite animatedSprite = new Sprite(frames);
            final Properties properties = new Properties(tileEntity.properties());
            final Optional<String> name = properties.get("name");
            if (!frames.isEmpty()) {
                addSprite(tilesetEntity.getFirstgid() + tileEntity.id(), animatedSprite);
            }
            if (name.isPresent()) {
                addNameToSprite(tilesetEntity.getFirstgid() + tileEntity.id(), name.get());
            }
        }
    }

    void addSprite(final int id, final Sprite sprite) {
        spritesById.put(id, sprite);
        allSprites.add(sprite);
    }

    public Sprite findById(final int id) {
        final Sprite sprite = spritesById.get(id);
        if (isNull(sprite)) {
            throw new IllegalArgumentException("sprite not found: " + id);
        }
        return sprite;
    }

    public int spriteCount() {
        return spritesById.size();
    }

    public Sprite findByName(final String name) {
        final Sprite sprite = spritesByName.get(name);
        if (isNull(sprite)) {
            throw new IllegalArgumentException("sprite not found: " + name);
        }
        return sprite;
    }

    public void addNameToSprite(final int id, final String name) {
        spritesByName.put(name, findById(id));
    }

    public List<Sprite> all() {
        return allSprites;
    }

    public Sprite single() {
        if (spriteCount() != 1) {
            throw new IllegalStateException("tileset has not exactly one sprite");
        }
        return allSprites.get(0);

    }

    // TODO: doc an test
    // TODO: non asset version
    public static Asset<Sprite> assetFromJson(String fileName, String name) {
        return Asset.asset(() -> fromJson(fileName).findByName(name));
    }

    // TODO: doc an test
    public static Asset<Sprite> assetFromJson(String fileName) {
        return Asset.asset(() -> fromJson(fileName).first());
    }

    // TODO: doc an test
    private Sprite first() {
        if (spriteCount() == 0) {
            throw new IllegalStateException("tileset has no sprite");
        }
        return allSprites.get(0);
    }
}