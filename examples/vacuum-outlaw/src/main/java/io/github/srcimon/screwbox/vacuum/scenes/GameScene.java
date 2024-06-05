package io.github.srcimon.screwbox.vacuum.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.MouseCursor;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Map;
import io.github.srcimon.screwbox.vacuum.cursor.DynamicCursorImageSystem;
import io.github.srcimon.screwbox.vacuum.deathpit.Deathpit;
import io.github.srcimon.screwbox.vacuum.deathpit.DeathpitSystem;
import io.github.srcimon.screwbox.vacuum.cursor.Cursor;
import io.github.srcimon.screwbox.vacuum.player.Player;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControlSystem;
import io.github.srcimon.screwbox.vacuum.player.movement.DashSystem;
import io.github.srcimon.screwbox.vacuum.player.movement.MovementControlSystem;
import io.github.srcimon.screwbox.vacuum.tiles.DecorTile;
import io.github.srcimon.screwbox.vacuum.tiles.WallTile;

public class GameScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.loop().unlockFps();
        engine.window().setCursor(MouseCursor.HIDDEN);
        engine.graphics().camera().setZoom(3.5);
        engine.graphics().light().setAmbientLight(Percent.of(0.2));
    }

    @Override
    public void populate(Environment environment) {
        Map map = Map.fromJson("maps/DemoLevel.json");

        environment.addSystem(engine -> {
            if (engine.keyboard().isPressed(Key.K)) {
                engine.graphics().configuration().toggleFullscreen();
            }
        });
        environment
                .addSystem(new DashSystem())
                .addSystem(new MovementControlSystem())
                .addSystem(new LogFpsSystem())
                .addSystem(new DeathpitSystem())
                .addSystem(new DynamicCursorImageSystem())
                .addSystem(new PlayerAttackControlSystem())
                .enableAllFeatures();

        environment.importSource(map)
                .as(tiledMap -> new Entity("world").add(new CameraBoundsComponent(tiledMap.bounds())));

        environment.importSource(map).as(new Cursor());
        //TODO environment .importOnce(new Cursor());

        environment.importSource(map.objects())
                .usingIndex(GameObject::name)
                .when("deathpit").as(new Deathpit())
                .when("player").as(new Player())
                //TODO .andAs(----)
                .when("light").as(object -> new Entity(object.id()).name("light")
                        .add(new TransformComponent(object.position()))
                        .add(new GlowComponent(18, Color.hex("#feffe9")))
                        .add(new PointLightComponent(120, Color.BLACK)));
        environment.importSource(map.tiles())
                .usingIndex(tile -> tile.layer().clazz())
                .when("wall").as(new WallTile())
                .when("decor").as(new DecorTile());
    }
}
