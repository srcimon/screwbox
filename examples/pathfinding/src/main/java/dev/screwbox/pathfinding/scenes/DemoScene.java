package dev.screwbox.pathfinding.scenes;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.ai.PathMovementDebugSystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.QuitOnKeySystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.navigation.NavigationRegionComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.pathfinding.components.PlayerMovementComponent;
import dev.screwbox.pathfinding.components.SpriteChangeComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;
import dev.screwbox.tiled.Tile;

import static dev.screwbox.core.Bounds.atPosition;
import static dev.screwbox.core.assets.Asset.asset;
import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

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
                .addSystemsFromPackage("dev.screwbox.pathfinding.systems")
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
                .add(new MotionRotationComponent())
                .add(new RenderComponent(object.layer().order()),
                        renderComponent -> renderComponent.options = renderComponent.options.sortOrthographic())
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsComponent())
                .add(new PathMovementComponent(50, 1000))
                .add(new MotionRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Converter<Tile> floor() {
        return tile -> new Entity()
                .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                .bounds(tile.bounds());
    }

    private Converter<Tile> wall() {
        return tile -> floor().convert(tile)
                .add(new ObstacleComponent())
                .add(new ColliderComponent());
    }

    private Converter<Map> worldInfoSingleton() {
        return map -> new Entity()
                .bounds(map.bounds())
                .add(new NavigationRegionComponent())
                .add(new CameraBoundsComponent(map.bounds()));
    }
}