package io.github.srcimon.screwbox.examples.pathfinding.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.*;
import io.github.srcimon.screwbox.core.entities.systems.*;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.examples.pathfinding.components.SpriteChangeComponent;
import io.github.srcimon.screwbox.examples.pathfinding.systems.EnemyMovementSystem;
import io.github.srcimon.screwbox.examples.pathfinding.systems.PlayerControlSystem;
import io.github.srcimon.screwbox.examples.pathfinding.systems.SpriteChangeSystem;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Map;
import io.github.srcimon.screwbox.tiled.Tile;

import static io.github.srcimon.screwbox.core.Bounds.atPosition;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.assets.Asset.asset;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DemoScene implements Scene {

    private static final Asset<Sprite> PLAYER_STANDING = spriteAssetFromJson("player.json", "standing");
    private static final Asset<Sprite> PLAYER_WALKING = spriteAssetFromJson("player.json", "walking");
    private static final Asset<Sprite> ENEMY_STANDING = spriteAssetFromJson("enemy.json", "standing");
    private static final Asset<Sprite> ENEMY_WALKING = spriteAssetFromJson("enemy.json", "walking");

    private static final Asset<Map> MAP = asset(() -> Map.fromJson("map.json"));

    @Override
    public void initialize(final Entities entities) {
        entities.importSource(MAP.get().tiles())
                .usingIndex(tile -> tile.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        entities.importSource(MAP.get())
                .as(worldBounds());

        entities.importSource(MAP.get().objects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("enemy").as(enemy())
                .when("camera").as(camera());

        entities.addSystem(new RenderSystem())
                .addSystem(new CameraMovementSystem())
                .addSystem(new StateSystem())
                .addSystem(new PlayerControlSystem())
                .addSystem(new AutoRotationSystem())
                .addSystem(new AutomovementSystem())
                .addSystem(new AutomovementDebugSystem())
                .addSystem(new QuitOnKeyPressSystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem())
                .addSystem(new PathfindingGridCreationSystem(16, Sheduler.withInterval(ofSeconds(1))))
                .addSystem(new EnemyMovementSystem())
                .addSystem(new SpriteChangeSystem())
                .addSystem(new PhysicsSystem());
    }

    private Converter<GameObject> camera() {
        return object -> new Entity()
                .add(new TransformComponent(object.bounds()))
                .add(new CameraComponent(2.5))
                .add(new CameraMovementComponent(2, object.properties().forceInt("target")));
    }

    private Converter<GameObject> player() {
        return object -> new Entity(object.id())
                .add(new SpriteChangeComponent(PLAYER_STANDING.get(), PLAYER_WALKING.get()))
                .add(new PlayerMovementComponent())
                .add(new PhysicsBodyComponent())
                .add(new AutoRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsBodyComponent())
                .add(new AutomovementComponent(30))
                .add(new AutoRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new RenderComponent(tile.sprite(), tile.layer().order()))
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