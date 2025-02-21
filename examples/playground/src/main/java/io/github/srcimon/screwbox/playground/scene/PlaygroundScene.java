package io.github.srcimon.screwbox.playground.scene;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import io.github.srcimon.screwbox.core.environment.core.EngineWatermarkSystem;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.scene.enemy.PatrollingEnemy;
import io.github.srcimon.screwbox.playground.scene.player.Player;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbSystem;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlSystem;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabSystem;
import io.github.srcimon.screwbox.playground.scene.world.Gravity;
import io.github.srcimon.screwbox.playground.scene.world.Ground;
import io.github.srcimon.screwbox.playground.scene.world.RenderBackgroundSystem;

public class PlaygroundScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(3);
    }

    @Override
    public void populate(Environment environment) {
        AsciiMap map = AsciiMap.fromString("""
                 #         #####                    ####  ##
                 #         ##### e                  ####  ##
                 #         #########                      #
                 #
                 #
                 #                                        e
                 #                     #                  ##     ##
                 #                     #                  ##     ##
                 #                     #                  ##     ##
                 #                     #                  ##     ##
                 #                     #                  ##     ##
                 #
                 #
                 #
                 #        p                          e
                 ######################              #####         ##############################
                 ######################  ####    #########    ###################################
                 ######################  #################    ###################################
                 ######################  #################    ###################################
                 ######################  #################    ###################################
                 ######################  #################    ###################################
                """, 8);

        environment
                .enableAllFeatures()
                .addSystem(new RenderBackgroundSystem())
                .addSystem(new GrabSystem())
                .addSystem(new ClimbSystem())
                .addSystem(new DashControlSystem())
                .addSystem(new EngineWatermarkSystem())
                .addSystem(new CrtMonitorOverlaySystem())
                .addSystem(new QuitEngineSystem())
                .addSystem(new ResetSceneSystem())
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('#').as(new Ground())
                .when('e').as(new PatrollingEnemy())
                .when('p').as(new Player());


        environment
                .importSource(map)
                .as(new Gravity());
    }
}
