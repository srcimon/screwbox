package dev.screwbox.vacuum.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ImportRuleset;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.navigation.NavigationRegionComponent;
import dev.screwbox.core.environment.navigation.NavigationSystem;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.window.MouseCursor;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Map;
import dev.screwbox.vacuum.cursor.Cursor;
import dev.screwbox.vacuum.cursor.DynamicCursorImageSystem;
import dev.screwbox.vacuum.deathpit.DeathPit;
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

import static dev.screwbox.core.environment.ImportRuleset.source;

public class GameScene implements Scene {

    private final Map map = Map.fromJson("maps/DemoLevel.json");

    @Override
    public void onEnter(Engine engine) {
        engine.window().setCursor(MouseCursor.HIDDEN);
        engine.graphics().camera().setZoom(3.5);
        engine.graphics().light().setAmbientLight(Percent.of(0.2)).setDefaultLensFlareNone();
    }

    @Override
    public void populate(final Environment environment) {
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
                .enableAllFeatures()

                .importSource(source(map)
                        .make(new Cursor())
                        .make(map -> new Entity("world")
                                .bounds(map.bounds())
                                .add(new CameraBoundsComponent())
                                .add(new NavigationRegionComponent())))

                .importSource(ImportRuleset.indexedSources(map.objects(), GameObject::name)
                        .assign("deathpit", new DeathPit())
                        .assign("player", new Player())
                        .assign("spawnpoint", new SpawnPoint())
                        .assign("light", new Light())
                        .assign("wall", new OrthographicWall()));

        environment.importSourceDEPRECATED(map.tiles())
                .usingIndex(tile -> tile.layer().clazz())
                .when("wall").as(new WallTile())
                .when("decor").as(new DecorTile());
    }
}
