package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.entities.*;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.StateComponent;
import io.github.srcimon.screwbox.core.entities.components.TimeoutComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.CatMarkerComponent;
import io.github.srcimon.screwbox.examples.platformer.components.NavpointComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.Tileset;
import io.github.srcimon.screwbox.examples.platformer.specials.player.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Order(SystemOrder.PREPARATION)
public class CatMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype CAT = Archetype.of(CatMarkerComponent.class, TransformComponent.class);
    private static final Archetype NAVPOINTS = Archetype.of(NavpointComponent.class, TransformComponent.class);

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
        Optional<Entity> catEntity = engine.entities().fetch(CAT);
        if (catEntity.isEmpty()) {
            return;
        }

        Entity player = engine.entities().forcedFetch(PLAYER);
        EntityState state = player.get(StateComponent.class).state;
        Vector playerPosition = player.get(TransformComponent.class).bounds.position();
        var flipMode = player.get(RenderComponent.class).flip;
        Entity navpoint = new Entity().add(
                new TransformComponent(Bounds.atPosition(playerPosition.addX(-10), 0, 0)),
                new TimeoutComponent(engine.loop().lastUpdate().plusMillis(200)),
                new NavpointComponent(state.getClass(), flipMode));

        engine.entities().addEntity(navpoint);

        List<Entity> navpoints = engine.entities().fetchAll(NAVPOINTS);
        if (navpoints.isEmpty()) {
            return;
        }
        Entity nextNavpoint = navpoints.get(0); // rely on implementation: first entity is always the oldest one
        NavpointComponent navpointComponent = nextNavpoint.get(NavpointComponent.class);
        Vector nextPosition = nextNavpoint.get(TransformComponent.class).bounds.position();
        Sprite nextSprite = SPRITES.get().get(navpointComponent.state);
        if (isNull(nextSprite)) {
            return;
        }
        Entity cat = catEntity.get();
        TransformComponent catBounds = cat.get(TransformComponent.class);
        catBounds.bounds = Bounds.atPosition(nextPosition, catBounds.bounds.width(), catBounds.bounds.height());
        RenderComponent renderComponent = cat.get(RenderComponent.class);
        renderComponent.sprite = nextSprite;
        renderComponent.flip = navpointComponent.flipMode;

    }

}
