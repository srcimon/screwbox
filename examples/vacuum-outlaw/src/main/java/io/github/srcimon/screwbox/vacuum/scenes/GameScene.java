package io.github.srcimon.screwbox.vacuum.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridConfigurationComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridUpdateSystem;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.window.MouseCursor;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Map;
import io.github.srcimon.screwbox.vacuum.cursor.Cursor;
import io.github.srcimon.screwbox.vacuum.cursor.DynamicCursorImageSystem;
import io.github.srcimon.screwbox.vacuum.deathpit.Deathpit;
import io.github.srcimon.screwbox.vacuum.deathpit.DeathpitSystem;
import io.github.srcimon.screwbox.vacuum.decoration.Light;
import io.github.srcimon.screwbox.vacuum.enemies.EnemySpawnSystem;
import io.github.srcimon.screwbox.vacuum.enemies.HurtSystem;
import io.github.srcimon.screwbox.vacuum.enemies.RunAtPlayerSystem;
import io.github.srcimon.screwbox.vacuum.enemies.SpawnPoint;
import io.github.srcimon.screwbox.vacuum.player.Player;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControlSystem;
import io.github.srcimon.screwbox.vacuum.player.movement.DashSystem;
import io.github.srcimon.screwbox.vacuum.player.movement.MovementControlSystem;
import io.github.srcimon.screwbox.vacuum.tiles.DecorTile;
import io.github.srcimon.screwbox.vacuum.tiles.WallTile;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class GameScene implements Scene {

    private final Map map = Map.fromJson("maps/DemoLevel.json");

    @Override
    public void onEnter(Engine engine) {
        engine.window().setCursor(MouseCursor.HIDDEN);
        engine.graphics().camera().setZoom(3.5);

        engine.graphics().light().setAmbientLight(Percent.of(0.2));
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
                .addSystem(new PhysicsGridUpdateSystem())
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
                        .add(new PhysicsGridConfigurationComponent(tiledMap.bounds(), 16, withInterval(ofSeconds(60)))))
                .as(new Cursor());

        environment.importSource(map.objects())
                .usingIndex(GameObject::name)
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
