package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Angle;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Sprite;

import java.util.List;

import static io.github.simonbas.screwbox.tiled.Tileset.fromJson;

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
