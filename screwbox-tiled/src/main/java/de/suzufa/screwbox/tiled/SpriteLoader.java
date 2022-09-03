package de.suzufa.screwbox.tiled;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.utils.ResourceLoader;
import de.suzufa.screwbox.tiled.internal.entity.FrameEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.TileEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

final class SpriteLoader {

    private SpriteLoader() {
    }

    public static Tileset loadTileset(final MapEntity map) {
        final Tileset tileset = new Tileset();

        for (final TilesetEntity mapTileset : map.getTilesets()) {
            addTilesToTileset(mapTileset, tileset);
        }
        return tileset;
    }

    public static void addTilesToTileset(final TilesetEntity tileset, final Tileset dictionary) {
        final String imageFileName = tileset.getImage();
        final BufferedImage tilesetImage = loadImageFrom(imageFileName);

        int localId = 0;

        // Read static images
        for (int y = 0; y < tileset.getImageheight(); y += tileset.getTileheight()) {
            for (int x = 0; x < tileset.getImagewidth(); x += tileset.getTilewidth()) {

                final BufferedImage subimage = tilesetImage.getSubimage(x, y, tileset.getTilewidth(),
                        tileset.getTileheight());
                final Sprite sprite = Sprite.fromImage(subimage);
                dictionary.addSprite(tileset.getFirstgid() + localId, sprite);
                localId++;
            }
        }

        for (final TileEntity tileEntity : tileset.getTiles()) {
            // read animated images and properties
            final List<Frame> frames = new ArrayList<>();
            for (final FrameEntity frame : tileEntity.animation()) {
                final Image currentImage = dictionary.findById(tileset.getFirstgid() + frame.tileid()).singleFrame()
                        .image();
                frames.add(new Frame(currentImage, Duration.ofMillis(frame.duration())));
            }
            final Sprite animatedSprite = new Sprite(frames);// TODO: add Frame per Frame / validate frame
                                                             // size
            // with others
            final Properties properties = new Properties(tileEntity.properties());
            final Optional<String> name = properties.get("name");
            if (!frames.isEmpty()) {
                dictionary.addSprite(tileset.getFirstgid() + tileEntity.id(), animatedSprite);
            }
            if (name.isPresent()) {
                dictionary.addNameToSprite(tileset.getFirstgid() + tileEntity.id(), name.get());
            }
        }
    }

    private static BufferedImage loadImageFrom(final String imageFileName) {
        final File file = ResourceLoader.resourceFile(imageFileName);
        try {
            return ImageIO.read(file);
        } catch (final IOException e) {
            throw new IllegalStateException("could not load tileset: " + file.getName());
        }
    }

}
