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
        defaultTile = Sprite.placeholder(Color.RED, tileWidth);
        tileset.put(16, new Sprite(frame.extractArea(Offset.at(0, 0), Size.square(tileWidth))));
        tileset.put(17, new Sprite(frame.extractArea(Offset.at(0, 1 * tileWidth), Size.square(tileWidth))));
        tileset.put(1, new Sprite(frame.extractArea(Offset.at(0, 2 * tileWidth), Size.square(tileWidth))));
        tileset.put(0, new Sprite(frame.extractArea(Offset.at(0, 3 * tileWidth), Size.square(tileWidth))));
        tileset.put(4, new Sprite(frame.extractArea(Offset.at(tileWidth, 3 * tileWidth), Size.square(tileWidth))));
        tileset.put(68, new Sprite(frame.extractArea(Offset.at(2*tileWidth, 3 * tileWidth), Size.square(tileWidth))));
        tileset.put(64, new Sprite(frame.extractArea(Offset.at(3*tileWidth, 3 * tileWidth), Size.square(tileWidth))));
    }

    public Sprite spriteForIndex(final int index) {
        var tile = tileset.get(index);
        return isNull(tile) ? defaultTile : tile;
    }
}
