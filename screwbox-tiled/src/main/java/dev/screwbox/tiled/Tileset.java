package dev.screwbox.tiled;

import dev.screwbox.core.Duration;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.tiled.internal.FrameEntity;
import dev.screwbox.tiled.internal.TileEntity;
import dev.screwbox.tiled.internal.TilesetEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Tileset {

    private final Map<Integer, Sprite> spritesById = new HashMap<>();
    private final Map<String, Sprite> spritesByName = new HashMap<>();
    private final List<Sprite> allSprites = new ArrayList<>();

    /**
     * Creates a {@link Tileset} from the specified resource file.
     * @param fileName name of the resource file.
     * @return created {@link Tileset}
     */
    public static Tileset fromJson(final String fileName) {
        final var tilesetEntity = TilesetEntity.load(fileName);
        return new Tileset(tilesetEntity);
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
                final Size imageSize = Size.of(tilesetEntity.getTilewidth(), tilesetEntity.getTileheight());
                final Frame subFrame = frame.extractArea(imageOffset, imageSize);
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
            if (!frames.isEmpty()) {
                final Sprite animatedSprite = new Sprite(frames);

                addSprite(tilesetEntity.getFirstgid() + tileEntity.id(), animatedSprite);
            }
            if (nonNull(tileEntity.type())) {
                addNameToSprite(tilesetEntity.getFirstgid() + tileEntity.id(), tileEntity.type());
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

    /**
     * Returns all {@link Sprite}s of this {@link Tileset}.
     *
     * @return all {@link Sprite}s
     */
    public List<Sprite> all() {
        return unmodifiableList(allSprites);
    }

    /**
     * Returns a single {@link Sprite} from the {@link Tileset}.
     * This only works with {@link Tileset}s containing exactly one {@link Sprite}.
     *
     * @return the only {@link Sprite} in this {@link Tileset}
     */
    public Sprite single() {
        if (spriteCount() != 1) {
            throw new IllegalStateException("tileset has not exactly one sprite");
        }
        return allSprites.getFirst();

    }

    /**
     * Returns the first {@link Sprite} of a {@link Tileset} directly from a file.
     *
     * @param fileName name of the Tileset file
     * @see #spriteFromJson(String, String)
     */
    public static Sprite spriteFromJson(final String fileName) {
        return fromJson(fileName).first();
    }

    /**
     * Returns a named {@link Sprite} directly from a {@link Tileset}.
     *
     * @param fileName name of the Tileset file
     * @param name     name of the {@link Sprite} inside the file.
     * @see #spriteFromJson(String)
     */
    public static Sprite spriteFromJson(final String fileName, final String name) {
        return fromJson(fileName).findByName(name);
    }

    /**
     * Returns the first {@link Sprite} of a {@link Tileset} directly from a file
     * wrapped as {@link Asset}.
     *
     * @param fileName name of the Tileset file
     * @see #spriteAssetFromJson(String, String)
     */
    public static Asset<Sprite> spriteAssetFromJson(final String fileName) {
        return Asset.asset(() -> spriteFromJson(fileName));
    }

    /**
     * Returns the first {@link Sprite} of a {@link Tileset} directly from a file
     * wrapped as {@link Asset}.
     *
     * @param fileName name of the {@link Tileset} file
     * @param name     name of the sprite within the {@link Tileset}
     * @see #spriteAssetFromJson(String)
     */
    public static Asset<Sprite> spriteAssetFromJson(final String fileName, final String name) {
        return Asset.asset(() -> spriteFromJson(fileName, name));
    }

    /**
     * Returns the first {@link Sprite} from the {@link Tileset}. Raises an
     * {@link IllegalStateException} when there is no {@link Sprite} in the
     * {@link Tileset}.
     */
    public Sprite first() {
        if (spriteCount() == 0) {
            throw new IllegalStateException("tileset has no sprite");
        }
        return allSprites.getFirst();
    }

    /**
     * Removes all {@link Sprite}s from the {@link Tileset}.
     */
    public void clear() {
        spritesById.clear();
        spritesByName.clear();
        allSprites.clear();
    }
}