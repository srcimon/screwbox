package dev.screwbox.pathfinding.scenes;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
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
import static dev.screwbox.core.environment.importing.ImportRuleset.indexedSources;
import static dev.screwbox.core.environment.importing.ImportRuleset.source;
import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class DemoScene implements Scene {

    private static final Asset<Sprite> PLAYER_STANDING = spriteAssetFromJson("player.json", "standing");
    private static final Asset<Sprite> PLAYER_WALKING = spriteAssetFromJson("player.json", "walking");
    private static final Asset<Sprite> ENEMY_STANDING = spriteAssetFromJson("enemy.json", "standing");
    private static final Asset<Sprite> ENEMY_WALKING = spriteAssetFromJson("enemy.json", "walking");

    private static final Asset<Map> MAP = asset(() -> Map.fromJson("map.json"));

    @Override
    public void populate(final Environment environment) {
        environment
                .enableAllFeatures()
                .addSystemsFromPackage("dev.screwbox.pathfinding.systems")
                .addSystem(new PathMovementDebugSystem())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem())

                .importSource(source(MAP.get())
                        .make(worldInfoSingleton()))

                .importSource(indexedSources(MAP.get().tiles(), tile -> tile.layer().name())
                        .assign("walls", wall())
                        .assign("floor", floor()))

                .importSource(indexedSources(MAP.get().objects(), GameObject::name)
                        .assign("player", player())
                        .assign("enemy", enemy()));
    }

    private Blueprint<GameObject> player() {
        return object -> new Entity(object.id())
                .add(new CameraTargetComponent())
                .add(new SpriteChangeComponent(PLAYER_STANDING.get(), PLAYER_WALKING.get()))
                .add(new PlayerMovementComponent())
                .add(new PhysicsComponent())
                .add(new MotionRotationComponent())
                .add(new RenderComponent(object.layer().order()), renderComponent -> renderComponent.isSortOrthographic = true)
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Blueprint<GameObject> enemy() {
        return object -> new Entity()
                .add(new SpriteChangeComponent(ENEMY_STANDING.get(), ENEMY_WALKING.get()))
                .add(new PhysicsComponent())
                .add(new PathMovementComponent(50, 1000))
                .add(new MotionRotationComponent())
                .add(new RenderComponent(object.layer().order()))
                .add(new TransformComponent(atPosition(object.position(), 8, 8)));
    }

    private Blueprint<Tile> floor() {
        return tile -> new Entity()
                .add(new RenderComponent(tile.sprite(), tile.layer().order()))
                .bounds(tile.bounds());
    }

    private Blueprint<Tile> wall() {
        return tile -> floor().assembleFrom(tile)
                .add(new ObstacleComponent())
                .add(new ColliderComponent());
    }

    private Blueprint<Map> worldInfoSingleton() {
        return map -> new Entity()
                .bounds(map.bounds())
                .add(new NavigationRegionComponent())
                .add(new CameraBoundsComponent());
    }
}