package dev.screwbox.core.generation;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

//TODO redesign interface between Converter Tile and AutoTile -> current interface sucks
//TODO move to own package
public class AutoTile {

    private enum MaskType {
        MASK_2X2,
        MASK_3X3;
    }

    private static final Map<Integer, Offset> MAPPINGS = Map.ofEntries(
            Map.entry(16, Offset.at(0, 0)),
            Map.entry(17, Offset.at(0, 1)),
            Map.entry(1, Offset.at(0, 2)),
            Map.entry(0, Offset.at(0, 3)),

            Map.entry(20, Offset.at(1, 0)),
            Map.entry(21, Offset.at(1, 1)),
            Map.entry(5, Offset.at(1, 2)),
            Map.entry(4, Offset.at(1, 3)),

            Map.entry(84, Offset.at(2, 0)),
            Map.entry(85, Offset.at(2, 1)),
            Map.entry(69, Offset.at(2, 2)),
            Map.entry(68, Offset.at(2, 3)),

            Map.entry(80, Offset.at(3, 0)),
            Map.entry(81, Offset.at(3, 1)),
            Map.entry(65, Offset.at(3, 2)),
            Map.entry(64, Offset.at(3, 3)),


            // full
            Map.entry(255, Offset.at(9, 2))
    );

    private final Map<Integer, Sprite> tileset = new HashMap<>();
    private final Sprite defaultTile; //Make empty sprite default sprite
    private final MaskType maskType;

    //TODO rename
    public static AutoTile fromSpriteSheet(final String fileName) {
        final var frame = Frame.fromFile(fileName);
        return new AutoTile(frame);
    }

    private AutoTile(final Frame frame) {
        Validate.isTrue(() -> frame.width() == 3 * frame.height(), "image width must be three times image height");
        this.maskType = MaskType.MASK_3X3;//TODO automatically detect maskType
        int tileWidth = frame.height() / 4;
        defaultTile = Sprite.placeholder(Color.RED.opacity(0.125), tileWidth);
        for (final var mapping : MAPPINGS.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
    }

    public Sprite spriteForIndex(final Mask mask) {
        final int index = MaskType.MASK_3X3.equals(this.maskType)
                ? mask.index3x3()
                : mask.index2x2();
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
            return boolToInt(north)
                   + boolToInt(east) * 4
                   + boolToInt(south) * 16
                   + boolToInt(west) * 64;
        }

        public int index3x3() {
            return index2x2()
                   + boolToInt(northEast && north && east) * 2
                   + boolToInt(southEast && east && south) * 8
                   + boolToInt(southWest && south && west) * 32
                   + boolToInt(northWest && west && north) * 128;
        }

        private int boolToInt(boolean value) {
            return value ? 1 : 0;
        }

        @Override
        public String toString() {
            return String.valueOf(index3x3());
        }
    }
}
