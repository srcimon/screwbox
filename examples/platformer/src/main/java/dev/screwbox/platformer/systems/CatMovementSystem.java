package dev.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dev.screwbox.core.Duration.ofMillis;
import static java.util.Objects.isNull;

@Order(Order.SystemOrder.PREPARATION)
public class CatMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype CAT = Archetype.ofSpacial(CatMarkerComponent.class);
    private static final Archetype NAVPOINTS = Archetype.ofSpacial(NavpointComponent.class);

    private static final Asset<Map<Class<?>, Sprite>> SPRITES = Asset.asset(() -> {
        Map<Class<?>, Sprite> sprites = new HashMap<>();
        Tileset catSprites = Tileset.fromJson("tilesets/specials/cat.json");
        Sprite walking = catSprites.findByName("walking");
        sprites.put(PlayerDeathState.class, walking);
        sprites.put(PlayerDiggingState.class, walking);
        sprites.put(PlayerFallingState.class, walking);
        sprites.put(PlayerFallThroughState.class, walking);
        sprites.put(PlayerRunningState.class, walking);
        sprites.put(PlayerIdleState.class, catSprites.findByName("idle"));
        sprites.put(PlayerJumpingStartedState.class, catSprites.findByName("jumping"));
        sprites.put(PlayerJumpingState.class, catSprites.findByName("jumping"));
        sprites.put(PlayerStandingState.class, catSprites.findByName("standing"));
        return sprites;
    });

    @Override
    public void update(Engine engine) {
        Optional<Entity> catEntity = engine.environment().tryFetchSingleton(CAT);
        if (catEntity.isEmpty()) {
            return;
        }

        Entity player = engine.environment().fetchSingleton(PLAYER);
        EntityState state = player.get(StateComponent.class).state;
        Vector playerPosition = player.position();
        var options = player.get(RenderComponent.class).options;
        Entity navpoint = new Entity().add(
                new TransformComponent(Bounds.atPosition(playerPosition.addX(-10), 0, 0)),
                new TweenDestroyComponent(),
                new TweenComponent(ofMillis(200)),
                new NavpointComponent(state.getClass(), options.isFlipHorizontal()));

        engine.environment().addEntity(navpoint);

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
        Entity cat = catEntity.get();
        cat.moveTo(nextPosition);
        RenderComponent renderComponent = cat.get(RenderComponent.class);
        renderComponent.sprite = nextSprite;
        renderComponent.options = renderComponent.options.flipHorizontal(navpointComponent.isFlippedHorizontally);
    }

}
