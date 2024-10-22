package io.github.srcimon.screwbox.platformer.scenes;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.platformer.menues.StartGameMenu;

import java.util.List;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteFromJson;

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
        Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(40));
        environment
                .enableTweening()
                .enablePhysics()
                .enableParticles()
                .enableRendering()
                .addSystem(engine -> {
                    Bounds visibleArea = engine.graphics().visibleArea();
                    if (sheduler.isTick()) {
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
                                .randomLifeTimeSeconds(2, 4)
                                .animateScale(0.5, 1.5)
                                .animateOpacity(Percent.zero(), Percent.max())
                                .sprites(BACKGROUNDS.get()));
                    }
                });
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new StartGameMenu());
    }

    @Override
    public void onExit(Engine engine) {
        engine.environment().clearEntities();
        engine.ui().closeMenu();
    }
}
