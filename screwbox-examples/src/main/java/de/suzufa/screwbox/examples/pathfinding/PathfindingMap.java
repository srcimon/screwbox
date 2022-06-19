package de.suzufa.screwbox.examples.pathfinding;

import static de.suzufa.screwbox.core.Bounds.atPosition;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class PathfindingMap {

    private final Map map;

    public PathfindingMap(final String name) {
        map = TiledSupport.loadMap("maze/map.json");
    }

    public void importInto(final EntityEngine entityEngine) {
        entityEngine.importSource(map.allTiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entityEngine.importSource(map)
                .as(worldBounds());

        entityEngine.importSource(map.allObjects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("camera").as(camera());

        entityEngine
                .add(new SpriteRenderSystem())
                .add(new CameraMovementSystem())
                .add(new PlayerMovementSystem())
                .add(new PhysicsSystem());
    }

    private Converter<GameObject> camera() {
        return object -> new Entity()
                .add(new TransformComponent(object.bounds()))
                .add(new CameraComponent(2.5))
                .add(new CameraMovementComponent(1, 1));
    }

    private Converter<GameObject> player() {
        return object -> new Entity(1)
                .add(new PhysicsBodyComponent())
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new SpriteComponent(tile.sprite(), tile.layer().order()))
                .add(new TransformComponent(tile.renderBounds()));
    }

    private Converter<Tile> wall() {
        return tile -> floor().convert(tile)
                .add(new ColliderComponent());
    }

    private Converter<Map> worldBounds() {
        return map -> new Entity()
                .add(new TransformComponent(map.bounds()))
                .add(new WorldBoundsComponent());
    }

}
