package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.ecosphere.*;
import io.github.srcimon.screwbox.core.ecosphere.components.ScreenTransitionComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.SignalComponent;
import io.github.srcimon.screwbox.core.graphics.transitions.*;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.ChangeMapComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ChangeMapSystem implements EntitySystem {

    private static final Archetype CHANGE_MAP_ZONES = Archetype.of(ChangeMapComponent.class, SignalComponent.class);

    private static final List<ScreenTransition> TRANSITIONS = List.of(
            new FadeOutTransition(new HorizontalLinesTransition(20)),
            new FadeOutTransition(new SwipeTransition()),
            new FadeOutTransition(new FadingScreenTransition()),
            new FadeOutTransition(new CircleTransition()),
            new FadeOutTransition(new MosaikTransition(30, 20)));

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.ecosphere().fetchAll(CHANGE_MAP_ZONES)) {
            ChangeMapComponent changeMapComponent = entity.get(ChangeMapComponent.class);
            if (changeMapComponent.time.isSet()) {
                if (Time.now().isAfter(changeMapComponent.time)) {
                    engine.scenes().add(new GameScene(entity.get(ChangeMapComponent.class).fileName));
                    engine.scenes().switchTo(GameScene.class);
                }
            } else if (engine.keyboard().isPressed(Key.SPACE) && entity.get(SignalComponent.class).isTriggered) {
                engine.ecosphere().addEntity(new Entity().add(
                        new ScreenTransitionComponent(randomFrom(TRANSITIONS), Duration.ofMillis(3200))));
                changeMapComponent.time = Time.now().plusSeconds(3);
            }

        }
    }

}
