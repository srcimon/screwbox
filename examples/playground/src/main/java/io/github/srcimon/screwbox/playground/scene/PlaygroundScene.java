package io.github.srcimon.screwbox.playground.scene;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.AirFrictionSystem;
import io.github.srcimon.screwbox.playground.movement.JumpSystem;
import io.github.srcimon.screwbox.playground.movement.MovementControlSystem;
import io.github.srcimon.screwbox.playground.scene.player.Player;
import io.github.srcimon.screwbox.playground.scene.world.Gravity;
import io.github.srcimon.screwbox.playground.scene.world.Ground;

public class PlaygroundScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(3);
    }

    @Override
    public void populate(Environment environment) {
        AsciiMap map = AsciiMap.fromString("""
                         #####                      ####  ##
                         #####                      ####  ##
                         #####                      ####  ##
                         
                         
                         
                         
                         
                         
                         p
                ##########################################################################
                """, 8);

        environment
                .enableAllFeatures()
                .addSystem(new MovementControlSystem())
                .addSystem(new QuitEngineSystem())
                .addSystem(new AirFrictionSystem())
                .addSystem(new JumpSystem())
                .addSystem(new ResetSceneSystem())
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Ground())
                .when('p').as(new Player());

        environment
                .importSource(map)
                .as(new Gravity());
    }
}
