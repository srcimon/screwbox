package de.suzufa.screwbox.examples.platformer.systems;

import static de.suzufa.screwbox.tiled.Tileset.fromJson;

import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;

public class StartBackgroundSystem implements EntitySystem {

    private static final Asset<List<Sprite>> BACKGROUNDS = Asset
            .asset(() -> List.of(
                    fromJson("tilesets/specials/player.json").findByName("idle"),
                    fromJson("tilesets/enemies/slime.json").findByName("moving"),
                    fromJson("tilesets/enemies/tracer.json").findByName("active"),
                    fromJson("tilesets/specials/cat.json").findByName("walking"),
                    fromJson("tilesets/collectables/cherries.json").first(),
                    fromJson("tilesets/props/box.json").first()));

    @Override
    public void update(final Engine engine) {
        final long milliseconds = engine.loop().runningTime().milliseconds();
        final int index = (int) ((milliseconds / 2000.0) % BACKGROUNDS.get().size());
        final Sprite sprite = BACKGROUNDS.get().get(index);
        final Offset center = engine.graphics().screen().center();

        final Angle rotation = Angle.degrees(milliseconds / 200.0);
        final double scale = Math.abs(Math.sin(milliseconds / 1000.0)) * 8 + 12;
        final double xCorrect = sprite.size().width() / 2.0 * scale;
        final double yCorrect = sprite.size().height() / 2.0 * scale;

        final Offset position = Offset.at(center.x() - xCorrect, center.y() - yCorrect);
        engine.graphics().screen().drawSprite(sprite, position, scale, Percent.half(), rotation);
    }

}
