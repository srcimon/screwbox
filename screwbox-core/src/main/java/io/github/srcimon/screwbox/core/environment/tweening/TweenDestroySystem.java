package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;

//TODO Tests and javadoc
public class TweenDestroySystem  implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenStateComponent.class, TweenDestroyComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tween : engine.environment().fetchAll(TWEENS)) {
           if(tween.get(TweenStateComponent.class).cycleCount > 0) {
               engine.environment().remove(tween);
           }
        }
    }
}
