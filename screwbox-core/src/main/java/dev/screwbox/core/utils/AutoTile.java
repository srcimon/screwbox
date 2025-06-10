package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

//TODO move to own package
public class AutoTile {

    private static final Map<Integer, Offset> MAPPINGS = Map.ofEntries(
            Map.entry(16, Offset.at(0, 0)),
            Map.entry(17, Offset.at(0, 1)),
            Map.entry(1, Offset.at(0, 2)),
            Map.entry(0, Offset.at(0, 3)),



            Map.entry(20, Offset.at(1, 0)),




            Map.entry(84, Offset.at(2, 0)),




            // full
            Map.entry(255, Offset.at(9, 2))
    );

    private Map<Integer, Sprite> tileset = new HashMap<>();
    private Sprite defaultTile;

    //TODO rename
    public static AutoTile fromDummyFile(final String fileName) {
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

    public Sprite spriteForIndex(final int index) {
        var tile = tileset.get(index);
        return isNull(tile) ? defaultTile : tile;
    }
}
