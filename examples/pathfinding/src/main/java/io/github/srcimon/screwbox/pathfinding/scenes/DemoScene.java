package io.github.srcimon.screwbox.pathfinding.scenes;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.ai.PathMovementComponent;
import io.github.srcimon.screwbox.core.environment.ai.PathMovementDebugSystem;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.QuitOnKeySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridConfigurationComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.MovementRotationComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.Scheduler;
import io.github.srcimon.screwbox.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.pathfinding.components.SpriteChangeComponent;
import dev.screwbox.tiles.GameObject;
import dev.screwbox.tiles.Map;
import dev.screwbox.tiles.Tile;

import static io.github.srcimon.screwbox.core.Bounds.atPosition;
import static io.github.srcimon.screwbox.core.assets.Asset.asset;
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