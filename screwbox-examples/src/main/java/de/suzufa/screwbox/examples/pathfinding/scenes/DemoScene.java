package de.suzufa.screwbox.examples.pathfinding.scenes;

import static de.suzufa.screwbox.core.Bounds.atPosition;
import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entityengine.components.AutomovementComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraComponent;
import de.suzufa.screwbox.core.entityengine.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PathfindingBlockingComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entityengine.systems.AutoRotationSystem;
import de.suzufa.screwbox.core.entityengine.systems.AutomovementDebugSystem;
import de.suzufa.screwbox.core.entityengine.systems.AutomovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entityengine.systems.PathfindingGridCreationSystem;
import de.suzufa.screwbox.core.entityengine.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entityengine.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entityengine.systems.StateSystem;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import de.suzufa.screwbox.examples.pathfinding.components.SpriteChangeComponent;
import de.suzufa.screwbox.examples.pathfinding.systems.EnemyMovementSystem;
import de.suzufa.screwbox.examples.pathfinding.systems.PlayerControlSystem;
import de.suzufa.screwbox.examples.pathfinding.systems.SpriteChangeSystem;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TiledSupport;

public class DemoScene implements Scene {

    private final Map map;

    public DemoScene(final String name) {
        map = TiledSupport.loadMap(name);
    }

    @Override
    public void initialize(final EntityEngine entityEngine) {
        importEntities(entityEngine);

        entityEngine
                .add(new SpriteRenderSystem())
                .add(new CameraMovementSystem())
                .add(new StateSystem())
                .add(new PlayerControlSystem())
                .add(new AutoRotationSystem())
                .add(new AutomovementSystem())
                .add(new AutomovementDebugSystem())
                .add(new PathfindingGridCreationSystem(16, Timer.withInterval(ofSeconds(1))))
                .add(new EnemyMovementSystem())
                .add(new SpriteChangeSystem())
                .add(new PhysicsSystem());
    }

    void importEntities(final EntityEngine entityEngine) {
        entityEngine.importSource(map.allTiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entityEngine.importSource(map)
                .as(worldBounds());

        entityEngine.importSource(map.allObjects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("enemy").as(enemy())
                .when("camera").as(camera());
    }

    private Converter<GameObject> camera() {
        return object -> new Entity()
                .add(new TransformComponent(object.bounds()))
                .add(new CameraComponent(2.5))
                .add(new CameraMovementComponent(2, object.properties().forceInt("target")));
    }

    private Converter<GameObject> player() {
        final var sprites = TiledSupport.loadTileset("maze/player.json");
        return object -> new Entity(object.id())
                .add(new SpriteChangeComponent(sprites.findByName("standing"), sprites.findByName("walking")))
                .add(new PlayerMovementComponent())
                .add(new PhysicsBodyComponent())
                .add(new AutoRotationComponent())
                .add(new SpriteComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        final var sprites = TiledSupport.loadTileset("maze/enemy.json");
        return object -> new Entity()
                .add(new SpriteChangeComponent(sprites.findByName("standing"), sprites.findByName("walking")))
                .add(new PhysicsBodyComponent())
                .add(new AutomovementComponent(30))
                .add(new AutoRotationComponent())
                .add(new SpriteComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new SpriteComponent(tile.sprite(), tile.layer().order()))
                .add(new TransformComponent(tile.renderBounds()));
    }

    private Converter<Tile> wall() {
        return tile -> floor().convert(tile)
                .add(new PathfindingBlockingComponent())
                .add(new ColliderComponent());
    }

    private static Converter<Map> worldBounds() {
        return map -> new Entity()
                .add(new TransformComponent(map.bounds()))
                .add(new WorldBoundsComponent());
    }

}
