package de.suzufa.screwbox.examples.pathfinding.scenes;

import static de.suzufa.screwbox.core.Bounds.atPosition;
import static de.suzufa.screwbox.core.Duration.ofSeconds;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entities.components.AutomovementComponent;
import de.suzufa.screwbox.core.entities.components.CameraComponent;
import de.suzufa.screwbox.core.entities.components.CameraMovementComponent;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.LightObstacleComponent;
import de.suzufa.screwbox.core.entities.components.PathfindingBlockingComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.entities.systems.AutoRotationSystem;
import de.suzufa.screwbox.core.entities.systems.AutomovementDebugSystem;
import de.suzufa.screwbox.core.entities.systems.AutomovementSystem;
import de.suzufa.screwbox.core.entities.systems.CameraMovementSystem;
import de.suzufa.screwbox.core.entities.systems.DrawLightSystem;
import de.suzufa.screwbox.core.entities.systems.LogFpsSystem;
import de.suzufa.screwbox.core.entities.systems.PathfindingGridCreationSystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsSystem;
import de.suzufa.screwbox.core.entities.systems.SpriteRenderSystem;
import de.suzufa.screwbox.core.entities.systems.StateSystem;
import de.suzufa.screwbox.core.graphics.Color;
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
import de.suzufa.screwbox.tiled.Tileset;

public class DemoScene implements Scene {

    private final Map map;

    public DemoScene(final String name) {
        map = Map.fromJson(name);
    }

    @Override
    public void initialize(final Entities entities) {
        importEntities(entities);

        entities.add(new SpriteRenderSystem())
                .add(new CameraMovementSystem())
                .add(new StateSystem())
                .add(new PlayerControlSystem())
                .add(new AutoRotationSystem())
                .add(new DrawLightSystem())
                .add(new AutomovementSystem())
                .add(new AutomovementDebugSystem())
                .add(new LogFpsSystem())
                .add(new PathfindingGridCreationSystem(16, Timer.withInterval(ofSeconds(1))))
                .add(new EnemyMovementSystem())
                .add(new SpriteChangeSystem())
                .add(new PhysicsSystem());
    }

    void importEntities(final Entities entities) {
        entities.importSource(map.tiles())
                .usingIndex(t -> t.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entities.importSource(map)
                .as(worldBounds());

        entities.importSource(map.objects())
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
        final var tileset = Tileset.fromJson("maze/player.json");
        return object -> new Entity(object.id())
                .add(new SpriteChangeComponent(tileset.findByName("standing"), tileset.findByName("walking")))
                .add(new PlayerMovementComponent())
                .add(new PhysicsBodyComponent())
                .add(new PointLightComponent(100, Color.RED))
                .add(new AutoRotationComponent())
                .add(new SpriteComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        final var sprites = Tileset.fromJson("maze/enemy.json");
        return object -> new Entity()
                .add(new SpriteChangeComponent(sprites.findByName("standing"), sprites.findByName("walking")))
                .add(new PhysicsBodyComponent())
                .add(new AutomovementComponent(30))
                .add(new AutoRotationComponent())
                .add(new PointLightComponent(80, Color.BLACK))
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
                .add(new LightObstacleComponent())
                .add(new ColliderComponent());
    }

    private static Converter<Map> worldBounds() {
        return map -> new Entity()
                .add(new TransformComponent(map.bounds()))
                .add(new WorldBoundsComponent());
    }
}