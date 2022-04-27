package de.suzufa.screwbox.playground.debo.specials.player;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;

public class PlayerIdleState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = PlayerResources.IDLE_SPRITE.newInstance();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        var control = entity.get(PlayerControlComponent.class);
        if (control.digPressed || control.jumpPressed || control.leftPressed || control.rightPressed) {
            return new PlayerStandingState();
        }
        return this;
    }

}
