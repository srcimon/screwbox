package dev.screwbox.core.graphics;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.utils.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        TEMPLATE_2X2(1, Mask::index2x2, Map.ofEntries(
                entry(4, Offset.at(0, 0)),
                entry(6, Offset.at(3, 0)),
                entry(2, Offset.at(1, 3)))),

        TEMPLATE_3X3(3, Mask::index3x3, Map.ofEntries(
                entry(16, Offset.at(0, 0)),
                entry(17, Offset.at(0, 1)),
                entry(1, Offset.at(0, 2)),
                entry(0, Offset.at(0, 3)),

                entry(20, Offset.at(1, 0)),
                entry(21, Offset.at(1, 1)),
                entry(5, Offset.at(1, 2)),
                entry(4, Offset.at(1, 3)),

                entry(84, Offset.at(2, 0)),
                entry(85, Offset.at(2, 1)),
                entry(69, Offset.at(2, 2)),
                entry(68, Offset.at(2, 3)),

                entry(80, Offset.at(3, 0)),
                entry(81, Offset.at(3, 1)),
                entry(65, Offset.at(3, 2)),
                entry(64, Offset.at(3, 3)),

                entry(213, Offset.at(4, 0)),
                entry(29, Offset.at(4, 1)),
                entry(23, Offset.at(4, 2)),
                entry(117, Offset.at(4, 3)),

                entry(92, Offset.at(5, 0)),
                entry(127, Offset.at(5, 1)),
                entry(223, Offset.at(5, 2)),
                entry(71, Offset.at(5, 3)),

                entry(116, Offset.at(6, 0)),
                entry(253, Offset.at(6, 1)),
                entry(247, Offset.at(6, 2)),
                entry(197, Offset.at(6, 3)),

                entry(87, Offset.at(7, 0)),
                entry(113, Offset.at(7, 1)),
                entry(209, Offset.at(7, 2)),
                entry(93, Offset.at(7, 3)),

                entry(28, Offset.at(8, 0)),
                entry(31, Offset.at(8, 1)),
                entry(95, Offset.at(8, 2)),
                entry(7, Offset.at(8, 3)),

                entry(125, Offset.at(9, 0)),
                entry(119, Offset.at(9, 1)),
                entry(255, Offset.at(9, 2)),
                entry(199, Offset.at(9, 3)),

                entry(124, Offset.at(10, 0)),
                entry(221, Offset.at(10, 2)),
                entry(215, Offset.at(10, 3)),

                entry(112, Offset.at(11, 0)),
                entry(245, Offset.at(11, 1)),
                entry(241, Offset.at(11, 2)),
                entry(193, Offset.at(11, 3))));

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
            return toInt(northEast)
                   + toInt(southEast) * 2
                   + toInt(southWest) * 4
                   + toInt(northWest) * 8;
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
            return String.valueOf(index3x3());
        }
    }
}
