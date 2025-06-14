package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.utils.Resources;
import dev.screwbox.core.utils.Validate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Map.entry;
import static java.util.Objects.isNull;

//TODO blogpost
//TODO guide
//TODO fix document reference for utils
//TODO redesign interface between Converter Tile and AutoTile -> current interface sucks
//TODO move to own package
public class AutoTile {

    public static Asset<AutoTile> assetFromSpriteSheet(final String fileName, final Template template) {
        return Asset.asset(() -> AutoTile.fromSpriteSheet(fileName, template));
    }

    public static AutoTile fromSpriteSheet(final String fileName, final Template template) {
        final var frame = Frame.fromFile(fileName);
        return new AutoTile(frame, template);
    }

    public enum Template {
        TEMPLATE_2X2(1, Mask::index2x2, loadTile("grass.properties")),

        TEMPLATE_3X3(3, Mask::index3x3, loadTile("rocks.properties"));

        private static Map<Integer, Offset> loadTile(String name) {
            Map<Integer, Offset> mmmm = new HashMap<>();
            try (var stream = Resources.getFileInputStream("assets/autotiles/" + name)) {
                Properties properties = new Properties();
                properties.load(stream);
                for(var entry : properties.entrySet()) {
                    var value = (String)entry.getValue();
                    var xy = value.split(",");
                    mmmm.put(Integer.parseInt((String)entry.getKey()), Offset.at(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return mmmm;
        }

        private final int aspectRatio;
        private final Function<Mask, Integer> indexFunction;
        private final Map<Integer, Offset> mappings;

        Template(final int aspectRatio, final Function<Mask, Integer> indexFunction, final Map<Integer, Offset> mappings) {
            this.aspectRatio = aspectRatio;
            this.indexFunction = indexFunction;
            this.mappings = mappings;
        }

    }

    private final Map<Integer, Sprite> tileset = new HashMap<>();
    private final Sprite defaultTile; //Make empty sprite default sprite
    //TODO DEFAULT TILE WONT BE NEEDED!!!!!!!!!
    private final Template template;

    private AutoTile(final Frame frame, Template template) {
        final int aspectRatio = frame.width() / frame.height();
        this.template = template;
        Validate.isTrue(() -> aspectRatio == template.aspectRatio, "aspect ratio of image doesn't match template");//TODO BETTER
        int tileWidth = frame.height() / 4;
        for (final var mapping : template.mappings.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
        defaultTile = Sprite.pixel(Color.RED).scaled(tileWidth); //TODO? new Sprite(frame.extractArea(Offset.at(10 * tileWidth, tileWidth), Size.square(tileWidth)));
    }

    public Sprite findSprite(final Mask mask) {
        final int index = template.indexFunction.apply(mask);
        final var tile = tileset.get(index);
        return isNull(tile)
                ? defaultTile
                : tile;
    }

    public static Mask createMask(final Offset tileOffset, Predicate<Offset> isNeighbour) {
        Objects.requireNonNull(tileOffset, "tile offset must not be null");
        return new Mask(
                isNeighbour.test(tileOffset.add(0, -1)),
                isNeighbour.test(tileOffset.add(1, -1)),
                isNeighbour.test(tileOffset.add(1, 0)),
                isNeighbour.test(tileOffset.add(1, 1)),
                isNeighbour.test(tileOffset.add(0, 1)),
                isNeighbour.test(tileOffset.add(-1, 1)),
                isNeighbour.test(tileOffset.add(-1, 0)),
                isNeighbour.test(tileOffset.add(-1, -1)));
    }

    public record Mask(boolean north, boolean northEast, boolean east, boolean southEast,
                       boolean south, boolean southWest, boolean west, boolean northWest) {

        public int index2x2() {
            return toInt(north)
                   + toInt(east) * 2
                   + toInt(south) * 4
                   + toInt(west) * 8;
        }

        public int index3x3() {
            return toInt(north)
                   + toInt(northEast && north && east) * 2
                   + toInt(east) * 4
                   + toInt(southEast && east && south) * 8
                   + toInt(south) * 16
                   + toInt(southWest && south && west) * 32
                   + toInt(west) * 64
                   + toInt(northWest && west && north) * 128;
        }

        private int toInt(boolean value) {
            return value ? 1 : 0;
        }

        @Override
        public String toString() {
            return String.valueOf(index2x2());
        }
    }
}
