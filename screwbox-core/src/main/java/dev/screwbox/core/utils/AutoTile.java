package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        private enum Neighbour {
            NORTH(0, -1),
            NORTH_EAST(1, -1),
            EAST(1, 0),
            SOUTH_EAST(1, 1),
            SOUTH(0, 1),
            SOUTH_WEST(-1, 1),
            WEST(-1, 0),
            NORTH_WEST(-1, -1);

            private final Offset offset;

            Neighbour(final int x, final int y) {
                offset = Offset.at(x, y);
            }
        }

        private final List<Neighbour> neighbours = new ArrayList<>();


        private Mask(Offset offset, Predicate<Offset> isNeighbor) {
            for (final var neighbour : Neighbour.values()) {
                var location = offset.add(neighbour.offset);
                if(isNeighbor.test(location)) {
                    neighbours.add(neighbour);
                }
            }
        }

        public int mask3x3() {
            final boolean[] vals = new boolean[8];
            for (final var neighbour : neighbours) {
                vals[neighbour.ordinal()] = true;
            }

            // invalidate edges to reduce autoTileIndex variety
            for (int i = 1; i < vals.length; i += 2) {
                int last = i -1;
                int next = i +1 > 7 ? 0 : i+1;
                if (!vals[last] || !vals[next]) {
                    vals[i] = false;
                }
            }

            double calculatedIndex = 0;
            for (int i = 0; i < vals.length; i++) {
                calculatedIndex += vals[i] ? Math.pow(2,i) : 0;
            }
            return  (int) calculatedIndex;
        }


        @Override
        public String toString() {
            return String.valueOf(mask3x3());
        }
    }
}
