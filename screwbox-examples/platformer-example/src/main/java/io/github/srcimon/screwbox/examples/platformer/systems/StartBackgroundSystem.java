package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Lurk;

import java.util.List;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.Percent.half;
import static io.github.srcimon.screwbox.tiled.Tileset.fromJson;

public class StartBackgroundSystem implements EntitySystem {

    private static final Asset<List<Sprite>> BACKGROUNDS = Asset
            .asset(() -> List.of(
                    fromJson("tilesets/specials/player.json").findByName("idle"),
                    fromJson("tilesets/enemies/slime.json").findByName("moving"),
                    fromJson("tilesets/enemies/tracer.json").findByName("active"),
                    fromJson("tilesets/specials/cat.json").findByName("walking"),
                    fromJson("tilesets/collectables/cherries.json").first(),
                    fromJson("tilesets/props/box.json").first()));

    private final Lurk rotationLurk = Lurk.intervalWithDeviation(ofSeconds(4), half());
    private final Lurk scaleLurk = Lurk.intervalWithDeviation(ofSeconds(4), half());
    private final Lurk xLurk = Lurk.intervalWithDeviation(ofSeconds(4), half());
    private final Lurk yLurk = Lurk.intervalWithDeviation(ofSeconds(4), half());

    @Override
    public void update(final Engine engine) {
        Time now = engine.loop().lastUpdate();
        final int index = (int) ((now.milliseconds() / 2000.0) % BACKGROUNDS.get().size());
        final Sprite sprite = BACKGROUNDS.get().get(index);
        final Offset center = engine.graphics().screen().center()
                .addX((int) (xLurk.value(now) * 200))
                .addY((int) (yLurk.value(now) * 100));

        final Rotation rotation = Rotation.degrees(rotationLurk.value(now) * 90);

        final double scale = Math.abs(scaleLurk.value(now)) * 5 + 10;
        final double xCorrect = sprite.size().width() / 2.0 * scale;
        final double yCorrect = sprite.size().height() / 2.0 * scale;

        final Offset position = Offset.at(center.x() - xCorrect, center.y() - yCorrect);
        engine.graphics().screen().drawSprite(sprite, position, scale, half(), rotation);
    }

}
