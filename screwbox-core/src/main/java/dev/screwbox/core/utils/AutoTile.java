package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

//TODO redesign interface between Converter Tile and AutoTile -> current interface sucks
//TODO move to own package
public class AutoTile {

    enum MaskType {
        MASK_2X2,
        MASK_3X3
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
            Map.entry(68, Offset.at(2, 3)),


            Map.entry(64, Offset.at(3, 3)),


            // full
            Map.entry(255, Offset.at(9, 2))
    );

    private Map<Integer, Sprite> tileset = new HashMap<>();
    private Sprite defaultTile; //Make empty sprite default sprite

    //TODO rename
    public static AutoTile fromSpriteSheet(final String fileName) {
        var frame = Frame.fromFile(fileName);
        return new AutoTile(frame);
    }


    private AutoTile(Frame frame) {
        Validate.isTrue(() -> frame.width() == 3 * frame.height(), "image width must be three times image height");
        int tileWidth = frame.height() / 4;
        defaultTile = Sprite.placeholder(Color.RED.opacity(0.125), tileWidth);
        for (final var mapping : MAPPINGS.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
    }

    public Sprite spriteForIndex(final Mask mask) {
        var tile = tileset.get(mask.mask3x3());
        return isNull(tile) ? defaultTile : tile;
    }

    public static Mask createMask(final Offset tileOffset, Predicate<Offset> isNeighbour) {
        return new Mask(tileOffset, isNeighbour);
    }

    public static class Mask {

        private final boolean north;
        private final boolean northEast;
        private final boolean east;
        private final boolean southEast;
        private final boolean south;
        private final boolean southWest;
        private final boolean west;
        private final boolean northWest;

        private Mask(Offset offset, Predicate<Offset> isNeighbor) {
            north = isNeighbor.test(offset.add(0, -1));
            northEast = isNeighbor.test(offset.add(1, -1));
            east = isNeighbor.test(offset.add(1, 0));
            southEast = isNeighbor.test(offset.add(1, 1));
            south = isNeighbor.test(offset.add(0, 1));
            southWest = isNeighbor.test(offset.add(-1, 1));
            west = isNeighbor.test(offset.add(-1, 0));
            northWest = isNeighbor.test(offset.add(-1, -1));
        }

        public int mask2x2() {
            return boolToInt(north)
                   + boolToInt(east) * 4
                   + boolToInt(south) * 16
                   + boolToInt(west) * 64;
        }

        public int mask3x3() {
            return mask2x2()
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
            return String.valueOf(mask3x3());
        }
    }
}
