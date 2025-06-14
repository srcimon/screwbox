package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.utils.Resources;
import dev.screwbox.core.utils.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

//TODO blogpost
//TODO guide
//TODO graphics

/**
 * The {@link AutoTile} is more or less a container for multiple {@link Sprite sprites} that will be used as tiles.
 * <p>
 * The {@link AutoTile} allows automatic detection of the right {@link Sprite} for a specific location.
 * The correct {@link Sprite} is detected by comparing the connected tiles to the tile at a specific offset.
 *
 * @since 3.5.0
 */
public class AutoTile {

    //TODO Reference to guide here
    /**
     * The template used for {@link AutoTile} creation. Currently two templates are available.
     * The template specifies the {@link Mask} method which is used to calculate the index of the {@link Sprite}
     * within the spritesheet that is used to initialize the {@link AutoTile}.
     */
    public enum Layout {

        /**
         * A less detailed tileset template. Copy the <a href="https://github.com/srcimon/screwbox/blob/master/screwbox-core/src/main/resources/assets/autotiles/template_2x2.png">template</a>
         * to your resource folder specify it as input for creating a new {@link AutoTile}. Uses 16 tiles.
         */
        LAYOUT_2X2(1, Mask::index2x2, "assets/autotiles/layout_2x2.properties"),

        /**
         * A more detailed tileset template. Copy the <a href="https://github.com/srcimon/screwbox/blob/master/screwbox-core/src/main/resources/assets/autotiles/template_3x3.png">template</a>
         * to your resource folder specify it as input for creating a new {@link AutoTile}. Uses 47 tiles.
         */
        LAYOUT_3X3(3, Mask::index3x3, "assets/autotiles/layout_3x3.properties");

        private final int aspectRatio;
        private final Function<Mask, Integer> index;
        private final String mappingProperties;

        Layout(final int aspectRatio, final Function<Mask, Integer> index, final String propertiesName) {
            this.aspectRatio = aspectRatio;
            this.index = index;
            this.mappingProperties = propertiesName;
        }
    }

    /**
     * The {@link AutoTile.Mask} is used to describe a specific tile location and to calculate the index of the
     * corresponding {@link Sprite} within the {@link AutoTile}.
     */
    public record Mask(boolean north, boolean northEast, boolean east, boolean southEast,
                       boolean south, boolean southWest, boolean west, boolean northWest) {

        /**
         * Returns the index for identifying {@link Sprite sprites} within the {@link AutoTile} by a 2 by 2 schema only
         * considering the four directly connected tiles.
         */
        public int index2x2() {
            return toInt(north)
                   + toInt(east) * 2
                   + toInt(south) * 4
                   + toInt(west) * 8;
        }

        /**
         * Returns the index for identifying {@link Sprite sprites} within the {@link AutoTile} by a 3 by 3 schema
         * considering all eight neighbour tiles.
         */
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

        private int toInt(final boolean value) {
            return value ? 1 : 0;
        }

        @Override
        public String toString() {
            return "Mask[2x2:%s / 3x3:%s]".formatted(index2x2(), index3x3());
        }
    }

    public static Mask createMask(final Offset offset, Predicate<Offset> isNeighbour) {
        Objects.requireNonNull(offset, "tile offset must not be null");
        return new Mask(
                isNeighbour.test(offset.add(0, -1)),
                isNeighbour.test(offset.add(1, -1)),
                isNeighbour.test(offset.add(1, 0)),
                isNeighbour.test(offset.add(1, 1)),
                isNeighbour.test(offset.add(0, 1)),
                isNeighbour.test(offset.add(-1, 1)),
                isNeighbour.test(offset.add(-1, 0)),
                isNeighbour.test(offset.add(-1, -1)));
    }

    public static Asset<AutoTile> assetFromSpriteSheet(final String fileName, final Layout layout) {
        return Asset.asset(() -> AutoTile.fromSpriteSheet(fileName, layout));
    }

    public static AutoTile fromSpriteSheet(final String fileName, final Layout layout) {
        final var frame = Frame.fromFile(fileName);
        return new AutoTile(frame, layout);
    }

    private final Map<Integer, Sprite> tileset = new HashMap<>();
    private final Layout layout;

    private AutoTile(final Frame frame, final Layout layout) {
        final int aspectRatio = frame.width() / frame.height();
        this.layout = Objects.requireNonNull(layout, "template must not be null");
        Validate.isTrue(() -> aspectRatio == layout.aspectRatio, "aspect ratio of image (%s:%s) doesn't match template (1:%s)"
                .formatted(frame.width(), frame.height(), 1 / layout.aspectRatio));//TODO Test message
        int tileWidth = frame.height() / 4;

        Map<Integer, Offset> mappings = new HashMap<>();
        for (final var entry : Resources.loadProperties(layout.mappingProperties).entrySet()) {
            var xy = entry.getValue().split(",");
            mappings.put(Integer.parseInt(entry.getKey()), Offset.at(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
        }

        for (final var mapping : mappings.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
    }

    public Sprite findSprite(final Mask mask) {
        final int index = layout.index.apply(mask);
        return tileset.get(index);
    }
}