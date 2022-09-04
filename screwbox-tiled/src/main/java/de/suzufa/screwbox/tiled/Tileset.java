package de.suzufa.screwbox.tiled;

import static java.util.Objects.isNull;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.utils.Resources;
import de.suzufa.screwbox.tiled.internal.FrameEntity;
import de.suzufa.screwbox.tiled.internal.TileEntity;
import de.suzufa.screwbox.tiled.internal.TilesetEntity;

public class Tileset {

    private final Map<Integer, Sprite> spritesById = new HashMap<>();
    private final Map<String, Sprite> spritesByName = new HashMap<>();
    private final List<Sprite> allSprites = new ArrayList<>();

    public static Tileset fromJson(final String fileName) {
        final var tilesetEntity = TilesetEntity.load(fileName);
        final var tileset = new Tileset(tilesetEntity);
        return tileset;
    }

    Tileset() {
    }

    Tileset(TilesetEntity tilesetEntity) {
        this(List.of(tilesetEntity));
    }

    Tileset(List<TilesetEntity> tilesetEntities) {
        for (final TilesetEntity tilesetEntity : tilesetEntities) {
            addTilesToTileset(tilesetEntity);
        }
    }

    public void addTilesToTileset(final TilesetEntity tilesetEntity) {
        final String imageFileName = tilesetEntity.getImage();
        final BufferedImage tilesetImage = loadImageFrom(imageFileName);

        int localId = 0;

        // Read static images
        for (int y = 0; y < tilesetEntity.getImageheight(); y += tilesetEntity.getTileheight()) {
            for (int x = 0; x < tilesetEntity.getImagewidth(); x += tilesetEntity.getTilewidth()) {

                final BufferedImage subimage = tilesetImage.getSubimage(x, y, tilesetEntity.getTilewidth(),
                        tilesetEntity.getTileheight());
                final Sprite sprite = Sprite.fromImage(subimage);
                addSprite(tilesetEntity.getFirstgid() + localId, sprite);
                localId++;
            }
        }

        for (final TileEntity tileEntity : tilesetEntity.getTiles()) {
            // read animated images and properties
            final List<Frame> frames = new ArrayList<>();
            for (final FrameEntity frame : tileEntity.animation()) {
                final Image currentImage = findById(tilesetEntity.getFirstgid() + frame.tileid()).singleFrame()
                        .image();
                frames.add(new Frame(currentImage, Duration.ofMillis(frame.duration())));
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

    private BufferedImage loadImageFrom(final String imageFileName) {
        final File file = Resources.loadFile(imageFileName);
        try {
            return ImageIO.read(file);
        } catch (final IOException e) {
            throw new IllegalStateException("could not load tileset: " + file.getName());
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
}
