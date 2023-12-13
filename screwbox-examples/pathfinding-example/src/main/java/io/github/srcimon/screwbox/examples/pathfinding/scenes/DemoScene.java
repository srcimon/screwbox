package io.github.srcimon.screwbox.examples.pathfinding.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.autocamera.CameraComponent;
import io.github.srcimon.screwbox.core.environment.autocamera.CameraMovementComponent;
import io.github.srcimon.screwbox.core.environment.autocamera.CameraUpdateSystem;
import io.github.srcimon.screwbox.core.environment.core.QuitOnKeySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.core.GlobalBoundsComponent;
import io.github.srcimon.screwbox.core.environment.debug.AutomovementDebugSystem;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
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
    public void populate(final Environment environment) {
        environment.importSource(MAP.get().tiles())
                .usingIndex(tile -> tile.layer().name())
                .when("walls").as(wall())
                .when("floor").as(floor());

        environment.importSource(MAP.get())
                .as(worldBounds());

        environment.importSource(MAP.get().objects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("enemy").as(enemy())
                .when("camera").as(camera());

        environment.addSystem(new RenderSystem())
                .addSystem(new CameraUpdateSystem())
                .addSystem(new StateSystem())
                .addSystem(new PlayerControlSystem())
                .addSystem(new RotateSpriteSystem())
                .addSystem(new AutomovementSystem())
                .addSystem(new AutomovementDebugSystem())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem())
                .addSystem(new PathfindingSystem(16, Sheduler.withInterval(ofSeconds(1))))
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
                .add(new PhysicsComponent())
                .add(new RotateSpriteComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsComponent())
                .add(new AutomovementComponent(30))
                .add(new RotateSpriteComponent())
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
                .add(new BlockPathComponent())
                .add(new ColliderComponent());
    }

    private static Converter<Map> worldBounds() {
        return map -> new Entity()
                .add(new TransformComponent(map.bounds()))
                .add(new GlobalBoundsComponent());
    }
}