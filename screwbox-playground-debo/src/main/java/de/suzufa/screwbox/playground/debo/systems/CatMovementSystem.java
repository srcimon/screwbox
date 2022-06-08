package de.suzufa.screwbox.playground.debo.systems;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;
import de.suzufa.screwbox.core.entityengine.components.TimeoutComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.CatMarkerComponent;
import de.suzufa.screwbox.playground.debo.components.NavpointComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerDeathState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerDiggingState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerFallThroughState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerFallingState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerIdleState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerJumpingStartedState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerJumpingState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerRunningState;
import de.suzufa.screwbox.playground.debo.specials.player.PlayerStandingState;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class CatMovementSystem implements EntitySystem {

    private static final String WALKING = "walking";
    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype CAT = Archetype.of(CatMarkerComponent.class, TransformComponent.class);
    private static final Archetype NAVPOINTS = Archetype.of(NavpointComponent.class, TransformComponent.class);

    private static final Map<Class<?>, Sprite> SPRITES = new HashMap<>();

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }

    static {
        SpriteDictionary catSprites = TiledSupport.loadTileset("tilesets/specials/cat.json");
        SPRITES.put(PlayerDeathState.class, catSprites.findByName(WALKING));
        SPRITES.put(PlayerDiggingState.class, catSprites.findByName(WALKING));
        SPRITES.put(PlayerFallingState.class, catSprites.findByName(WALKING));
        SPRITES.put(PlayerFallThroughState.class, catSprites.findByName(WALKING));
        SPRITES.put(PlayerIdleState.class, catSprites.findByName("idle"));
        SPRITES.put(PlayerJumpingStartedState.class, catSprites.findByName("jumping"));
        SPRITES.put(PlayerJumpingState.class, catSprites.findByName("jumping"));
        SPRITES.put(PlayerRunningState.class, catSprites.findByName(WALKING));
        SPRITES.put(PlayerStandingState.class, catSprites.findByName("standing"));
    }

    @Override
    public void update(Engine engine) {
        Optional<Entity> catEntity = engine.entityEngine().fetchSingle(CAT);
        if (catEntity.isEmpty()) {
            return;
        }

        Entity player = engine.entityEngine().forcedFetchSingle(PLAYER);
        EntityState state = player.get(StateComponent.class).state;
        Vector playerPosition = player.get(TransformComponent.class).bounds.position();
        boolean flipped = player.get(SpriteComponent.class).sprite.isFlippedHorizontally();
        Entity navpoint = new Entity().add(
                new TransformComponent(Bounds.atPosition(playerPosition.addX(-10), 0, 0)),
                new TimeoutComponent(engine.loop().metrics().timeOfLastUpdate().plusMillis(200)),
                new NavpointComponent(state.getClass(), flipped));

        engine.entityEngine().add(navpoint);

        List<Entity> navpoints = engine.entityEngine().fetchAll(NAVPOINTS);
        if (navpoints.isEmpty()) {
            return;
        }
        Entity nextNavpoint = navpoints.get(0); // rely on implementation: first entity is always the oldest one
        NavpointComponent navpointComponent = nextNavpoint.get(NavpointComponent.class);
        Vector nextPosition = nextNavpoint.get(TransformComponent.class).bounds.position();
        Sprite nextSprite = SPRITES.get(navpointComponent.state);
        if (isNull(nextSprite)) {
            return;
        }
        nextSprite.setFlippedHorizontally(navpointComponent.flipped);
        Entity cat = catEntity.get();
        TransformComponent catBounds = cat.get(TransformComponent.class);
        Bounds updatedBounds = Bounds.atPosition(nextPosition, catBounds.bounds.width(), catBounds.bounds.height());
        catBounds.bounds = updatedBounds;
        cat.get(SpriteComponent.class).sprite = nextSprite;

    }

}
