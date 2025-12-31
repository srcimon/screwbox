package dev.screwbox.platformer.scenes;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.platformer.menues.StartGameMenu;

import java.util.List;

import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.tiled.Tileset.spriteFromJson;

public class StartScene implements Scene {

    private static final Asset<List<Sprite>> BACKGROUNDS = Asset.asset(() -> List.of(
        spriteFromJson("tilesets/specials/player.json", "idle"),
        spriteFromJson("tilesets/enemies/slime.json", "moving"),
        spriteFromJson("tilesets/enemies/tracer.json", "active"),
        spriteFromJson("tilesets/specials/cat.json", "walking"),
        spriteFromJson("tilesets/collectables/cherries.json"),
        spriteFromJson("tilesets/props/box.json")));

    @Override
    public void populate(Environment environment) {
        Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(40));
        environment
            .enableAllFeatures()
            .addSystem(engine -> {
                Bounds visibleArea = engine.graphics().visibleArea();
                if (scheduler.isTick()) {
                    engine.particles().spawnMultiple(1, visibleArea.moveBy(0, visibleArea.height()), ParticleOptions.unknownSource()
                        .baseSpeed(Vector.y(-60))
                        .ease(Ease.SINE_IN_OUT)
                        .randomStartScale(6, 8)
                        .startOpacity(Percent.zero())
                        .animateOpacity(Percent.zero(), Percent.of(0.1))
                        .chaoticMovement(50, ofSeconds(1))
                        .drawOrder(2)
                        .animateHorizontalSpin()
                        .randomStartRotation()
                        .randomLifespanSeconds(2, 4)
                        .animateScale(0.5, 1.5)
                        .animateOpacity(Percent.zero(), Percent.max())
                        .sprites(BACKGROUNDS.get()));
                }
            });
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new StartGameMenu());
        engine.graphics()
            .disableSplitScreenMode()
            .configuration().setLightEnabled(false);
    }

    @Override
    public void onExit(Engine engine) {
        engine.environment().clearEntities();
        engine.ui().closeMenu();
    }
}
