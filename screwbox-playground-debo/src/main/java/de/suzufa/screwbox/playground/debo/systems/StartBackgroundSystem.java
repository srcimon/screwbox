package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.tiled.TiledSupport.loadTileset;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;

public class StartBackgroundSystem implements EntitySystem {

    private static final List<Sprite> BACKGROUNDS = new ArrayList<>();

    static {
        BACKGROUNDS.add(loadTileset("tilesets/specials/player.json").findByName("idle"));
        BACKGROUNDS.add(loadTileset("tilesets/enemies/slime.json").findByName("moving"));
        BACKGROUNDS.add(loadTileset("tilesets/enemies/tracer.json").findByName("active"));
        BACKGROUNDS.add(loadTileset("tilesets/specials/cat.json").findByName("walking"));
        BACKGROUNDS.add(loadTileset("tilesets/collectables/cherries.json").findById(0));
        BACKGROUNDS.add(loadTileset("tilesets/props/box.json").findById(0));
    }

    @Override
    public void update(final Engine engine) {
        final long milliseconds = engine.loop().metrics().durationOfRuntime().milliseconds();
        final int index = (int) ((milliseconds / 2000.0) % BACKGROUNDS.size());
        final Sprite sprite = BACKGROUNDS.get(index);
        final Offset center = engine.graphics().window().center();

        final Rotation rotation = Rotation.ofDegrees(milliseconds / 200.0);
        final double scale = Math.abs(Math.sin(milliseconds / 1000.0)) * 8 + 12;
        final double xCorrect = sprite.dimension().width() / 2.0 * scale;
        final double yCorrect = sprite.dimension().height() / 2.0 * scale;

        final Offset position = Offset.at(center.x() - xCorrect, center.y() - yCorrect);
        engine.graphics().window().drawSprite(sprite, position, scale, Percentage.half(), rotation);
    }

}
