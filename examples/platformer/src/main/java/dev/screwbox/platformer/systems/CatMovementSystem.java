package dev.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.CatMarkerComponent;
import dev.screwbox.platformer.components.NavpointComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.platformer.specials.player.PlayerDeathState;
import dev.screwbox.platformer.specials.player.PlayerDiggingState;
import dev.screwbox.platformer.specials.player.PlayerFallThroughState;
import dev.screwbox.platformer.specials.player.PlayerFallingState;
import dev.screwbox.platformer.specials.player.PlayerIdleState;
import dev.screwbox.platformer.specials.player.PlayerJumpingStartedState;
import dev.screwbox.platformer.specials.player.PlayerJumpingState;
import dev.screwbox.platformer.specials.player.PlayerRunningState;
import dev.screwbox.platformer.specials.player.PlayerStandingState;
import dev.screwbox.tiled.Tileset;

import java.util.List;
import java.util.Map;

import static dev.screwbox.core.Duration.ofMillis;
import static java.util.Objects.isNull;

@ExecutionOrder(Order.PREPARATION)
public class CatMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype CAT = Archetype.ofSpacial(CatMarkerComponent.class);
    private static final Archetype NAVPOINTS = Archetype.ofSpacial(NavpointComponent.class);

    private static final Asset<Map<Class<?>, Sprite>> SPRITES = Asset.asset(() -> {

        Tileset catSprites = Tileset.fromJson("tilesets/specials/cat.json");
        Sprite walking = catSprites.findByName("walking");
        return Map.of(
                PlayerDeathState.class, walking,
                PlayerDiggingState.class, walking,
                PlayerFallingState.class, walking,
                PlayerFallThroughState.class, walking,
                PlayerRunningState.class, walking,
                PlayerIdleState.class, catSprites.findByName("idle"),
                PlayerJumpingStartedState.class, catSprites.findByName("jumping"),
                PlayerJumpingState.class, catSprites.findByName("jumping"),
                PlayerStandingState.class, catSprites.findByName("standing")
        );
    });

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(CAT).ifPresent(cat -> {
            Entity player = engine.environment().fetchSingleton(PLAYER);
            EntityState state = player.get(StateComponent.class).state;
            Vector playerPosition = player.position();
            var options = player.get(RenderComponent.class).options;

            engine.environment().addEntity(new Entity()
                    .bounds(Bounds.atPosition(playerPosition.addX(-10), 0, 0))
                    .add(new TweenDestroyComponent())
                    .add(new TweenComponent(ofMillis(200)))
                    .add(new NavpointComponent(state.getClass(), options.isFlippedHorizontal())));

            List<Entity> navpoints = engine.environment().fetchAll(NAVPOINTS);
            if (navpoints.isEmpty()) {
                return;
            }
            Entity nextNavpoint = navpoints.getFirst();
            NavpointComponent navpointComponent = nextNavpoint.get(NavpointComponent.class);
            Vector nextPosition = nextNavpoint.position();
            Sprite nextSprite = SPRITES.get().get(navpointComponent.state);
            if (isNull(nextSprite)) {
                return;
            }
            cat.moveTo(nextPosition);
            RenderComponent renderComponent = cat.get(RenderComponent.class);
            renderComponent.sprite = nextSprite;
            renderComponent.options = renderComponent.options.flippedHorizontal(navpointComponent.isFlippedHorizontally);
        });

    }

}
