package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementComponent;
import io.github.srcimon.screwbox.vacuum.player.PlayerComponent;

public class RunAtPlayerSystem implements EntitySystem {

    private static final Archetype RUNNERS = Archetype.of(RunAtPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PlayerComponent.class).ifPresent(player -> {
            for (final var runner : engine.environment().fetchAll(RUNNERS)) {
                if(runner.get(RunAtPlayerComponent.class).refreshPathSheduler.isTick()) {
                    engine.async().runExclusive(runner, () ->
                            engine.physics().findPath(runner.position(), player.position()).ifPresent(
                                    path -> runner.get(AutomovementComponent.class).path = path));
                }
            }
        });


    }
}
