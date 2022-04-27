package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.window.WindowSprite.sprite;
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
    public void update(Engine engine) {
        long milliseconds = engine.loop().metrics().durationOfRuntime().milliseconds();
        int index = (int) ((milliseconds / 2000.0) % BACKGROUNDS.size());
        Sprite sprite = BACKGROUNDS.get(index);
        Offset center = engine.graphics().window().center();

        Rotation rotation = Rotation.ofDegrees(milliseconds / 200.0);
        double scale = Math.abs(Math.sin(milliseconds / 1000.0)) * 8 + 12;
        double xCorrect = sprite.dimension().width() / 2.0 * scale;
        double yCorrect = sprite.dimension().height() / 2.0 * scale;

        Offset position = Offset.at(center.x() - xCorrect, center.y() - yCorrect);
        engine.graphics().window().draw(sprite(sprite, position, scale, Percentage.of(0.5), rotation));

    }

}
