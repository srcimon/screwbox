package dev.screwbox.vacuum.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.navigation.NavigationRegionComponent;
import dev.screwbox.core.environment.navigation.NavigationSystem;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.window.MouseCursor;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;
import dev.screwbox.vacuum.cursor.Cursor;
import dev.screwbox.vacuum.cursor.DynamicCursorImageSystem;
import dev.screwbox.vacuum.deathpit.Deathpit;
import dev.screwbox.vacuum.deathpit.DeathpitSystem;
import dev.screwbox.vacuum.decoration.Light;
import dev.screwbox.vacuum.decoration.OrthographicWall;
import dev.screwbox.vacuum.enemies.EnemySpawnSystem;
import dev.screwbox.vacuum.enemies.HurtSystem;
import dev.screwbox.vacuum.enemies.RunAtPlayerSystem;
import dev.screwbox.vacuum.enemies.SpawnPoint;
import dev.screwbox.vacuum.player.Player;
import dev.screwbox.vacuum.player.attack.PlayerAttackControlSystem;
import dev.screwbox.vacuum.player.movement.DashSystem;
import dev.screwbox.vacuum.player.movement.MovementControlSystem;
import dev.screwbox.vacuum.tiles.DecorTile;
import dev.screwbox.vacuum.tiles.WallTile;

import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.core.utils.Scheduler.withInterval;

public class GameScene implements Scene {

    private final Map map = Map.fromJson("maps/DemoLevel.json");

    @Override
    public void onEnter(Engine engine) {
        engine.window().setCursor(MouseCursor.HIDDEN);
        engine.graphics().camera().setZoom(3.5);
        engine.graphics().light().setAmbientLight(Percent.of(0.2)).setDefaultLensFlareNone();
    }

    @Override
    public void populate(Environment environment) {
        environment.addSystem(engine -> {
            if (engine.keyboard().isPressed(Key.K)) {
                engine.graphics().configuration().toggleFullscreen();
            }
        });
        environment
                .addSystem(new DashSystem())
                .addSystem(new MovementControlSystem())
                .addSystem(new LogFpsSystem())
                .addSystem(new NavigationSystem())
                .addSystem(new HurtSystem())
                .addSystem(new RunAtPlayerSystem())
                .addSystem(new EnemySpawnSystem())
                .addSystem(new DeathpitSystem())
                .addSystem(new DynamicCursorImageSystem())
                .addSystem(new PlayerAttackControlSystem())
                .enableAllFeatures();

        environment.importSource(map)
                .as(tiledMap -> new Entity("world")
                        .add(new CameraBoundsComponent(tiledMap.bounds()))
                        .add(new NavigationRegionComponent(tiledMap.bounds(), 16, withInterval(ofSeconds(60)))))
                .as(new Cursor());

        environment.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("wall").as(new OrthographicWall())
                .when("deathpit").as(new Deathpit())
                .when("player").as(new Player())
                .when("spawnpoint").as(new SpawnPoint())
                .when("light").as(new Light());

        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().clazz())
                .when("wall").as(new WallTile())
                .when("decor").as(new DecorTile());
    }
}
