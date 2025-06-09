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
            Map.entry(16, Offset.at(0,0)),
            Map.entry(17, Offset.at(0,1)),
            Map.entry(1, Offset.at(0,2)),
            Map.entry(0, Offset.at(0,3)),

            Map.entry(4, Offset.at(1,3)),
            Map.entry(68, Offset.at(2,3)),
            Map.entry(64, Offset.at(3,3)),
            Map.entry(127, Offset.at(5,1)),
            Map.entry(253, Offset.at(6,1)),
            Map.entry(223, Offset.at(5,2)),
            Map.entry(247, Offset.at(6,2)),
            Map.entry(28, Offset.at(8,0)),
            Map.entry(125, Offset.at(9,0)),
            Map.entry(124, Offset.at(10,0)),
            Map.entry(112, Offset.at(11,0)),
            Map.entry(255, Offset.at(9,2)),
            Map.entry(20, Offset.at(1,0)),
            Map.entry(80, Offset.at(3,0)),
            Map.entry(5, Offset.at(1,2)),
            Map.entry(65, Offset.at(3,2)),
            Map.entry(85, Offset.at(2,1)),
            Map.entry(92, Offset.at(5,0)),
            Map.entry(116, Offset.at(6,0)),
            Map.entry(87, Offset.at(7,0)),
            Map.entry(29, Offset.at(4,1)),
            Map.entry(23, Offset.at(4,2)),
            Map.entry(117, Offset.at(4,3)),
            Map.entry(71, Offset.at(5,3)),
            Map.entry(197, Offset.at(6,3)),
            Map.entry(93, Offset.at(7,3)),
            Map.entry(31, Offset.at(8,1)),
            Map.entry(95, Offset.at(8,2)),
            Map.entry(7, Offset.at(8,3)),
            Map.entry(199, Offset.at(9,3)),
            Map.entry(215, Offset.at(10,3)),
            Map.entry(193, Offset.at(11,3)),
            Map.entry(119, Offset.at(9,1)),
            Map.entry(241, Offset.at(11,2)),
            Map.entry(245, Offset.at(11,1)),
            Map.entry(213, Offset.at(4,0))
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
        for(final  var mapping : MAPPINGS.entrySet()) {
            Offset value = mapping.getValue();
            tileset.put(mapping.getKey(), new Sprite(frame.extractArea(Offset.at(value.x() * tileWidth, value.y() * tileWidth), Size.square(tileWidth))));
        }
    }

    public Sprite spriteForIndex(final int index) {
        var tile = tileset.get(index);
        return isNull(tile) ? defaultTile : tile;
    }
}
