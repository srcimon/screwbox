package de.suzufa.screwbox.playground.debo.systems;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Tileset;

public class StartBackgroundSystem implements EntitySystem {

    private static final List<Sprite> BACKGROUNDS = new ArrayList<>();

    static {
        BACKGROUNDS.add(Tileset.fromJson("tilesets/specials/player.json").findByName("idle"));
        BACKGROUNDS.add(Tileset.fromJson("tilesets/enemies/slime.json").findByName("moving"));
        BACKGROUNDS.add(Tileset.fromJson("tilesets/enemies/tracer.json").findByName("active"));
        BACKGROUNDS.add(Tileset.fromJson("tilesets/specials/cat.json").findByName("walking"));
        BACKGROUNDS.add(Tileset.fromJson("tilesets/collectables/cherries.json").findById(0));
        BACKGROUNDS.add(Tileset.fromJson("tilesets/props/box.json").findById(0));
    }

    @Override
    public void update(final Engine engine) {
        final long milliseconds = engine.loop().runningTime().milliseconds();
        final int index = (int) ((milliseconds / 2000.0) % BACKGROUNDS.size());
        final Sprite sprite = BACKGROUNDS.get(index);
        final Offset center = engine.graphics().window().center();

        final Rotation rotation = Rotation.ofDegrees(milliseconds / 200.0);
        final double scale = Math.abs(Math.sin(milliseconds / 1000.0)) * 8 + 12;
        final double xCorrect = sprite.size().width() / 2.0 * scale;
        final double yCorrect = sprite.size().height() / 2.0 * scale;

        final Offset position = Offset.at(center.x() - xCorrect, center.y() - yCorrect);
        engine.graphics().window().drawSprite(sprite, position, scale, Percentage.half(), rotation, FlipMode.NONE);
    }

}
