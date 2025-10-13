package dev.screwbox.vacuum.enemies;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.vacuum.player.PlayerComponent;

public class RunAtPlayerSystem implements EntitySystem {

    private static final Archetype RUNNERS = Archetype.of(RunAtPlayerComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PlayerComponent.class).ifPresent(player -> {
            for (final var runner : engine.environment().fetchAll(RUNNERS)) {
                if(runner.get(RunAtPlayerComponent.class).refreshPathScheduler.isTick()) {
                    engine.async().runExclusive(runner, () ->
                            engine.navigation().findPath(runner.position(), player.position()).ifPresent(
                                    path -> runner.get(PathMovementComponent.class).path = path));
                }
            }
        });


    }
}
