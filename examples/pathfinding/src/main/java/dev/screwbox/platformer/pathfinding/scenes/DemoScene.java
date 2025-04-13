package dev.screwbox.platformer.pathfinding.scenes;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.ai.PathMovementDebugSystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.QuitOnKeySystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.PhysicsGridConfigurationComponent;
import dev.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.MovementRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.platformer.pathfinding.components.PlayerMovementComponent;
import dev.screwbox.platformer.pathfinding.components.SpriteChangeComponent;
import dev.screwbox.tiles.GameObject;
import dev.screwbox.tiles.Map;
import dev.screwbox.tiles.Tile;

import static dev.screwbox.core.Bounds.atPosition;
import static dev.screwbox.core.assets.Asset.asset;
import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

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
                .as(worldInfoSingleton());

        environment.importSource(MAP.get().objects())
                .usingIndex(GameObject::name)
                .when("player").as(player())
                .when("enemy").as(enemy());

        environment
                .enableAllFeatures()
                .addSystemsFromPackage("io.github.srcimon.screwbox.pathfinding.systems")
                .addSystem(new PathMovementDebugSystem())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem());
    }

    private Converter<GameObject> player() {
        return object -> new Entity(object.id())
                .add(new CameraTargetComponent())
                .add(new SpriteChangeComponent(PLAYER_STANDING.get(), PLAYER_WALKING.get()))
                .add(new PlayerMovementComponent())
                .add(new PhysicsComponent())
                .add(new MovementRotationComponent())
                .add(new RenderComponent(object.layer().order()),
                        renderComponent -> renderComponent.options = renderComponent.options.sortOrthographic())
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsComponent())
                .add(new PathMovementComponent(50, 1000))
                .add(new MovementRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                .bounds(tile.renderBounds());
    }

    private Converter<Tile> wall() {
        return tile -> floor().convert(tile)
                .add(new PhysicsGridObstacleComponent())
                .add(new ColliderComponent());
    }

    private Converter<Map> worldInfoSingleton() {
        return map -> new Entity()
                .add(new PhysicsGridConfigurationComponent(map.bounds(), 16, Scheduler.everySecond()))
                .add(new CameraBoundsComponent(map.bounds()));
    }
}