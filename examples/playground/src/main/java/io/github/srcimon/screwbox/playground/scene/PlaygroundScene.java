package io.github.srcimon.screwbox.playground.scene;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playground.movement.AirFrictionSystem;
import io.github.srcimon.screwbox.playground.movement.ClimbSystem;
import io.github.srcimon.screwbox.playground.movement.GrabSystem;
import io.github.srcimon.screwbox.playground.movement.JumpControlSystem;
import io.github.srcimon.screwbox.playground.movement.MovementControlSystem;
import io.github.srcimon.screwbox.playground.movement.WallJumpControlSystem;
import io.github.srcimon.screwbox.playground.scene.player.Player;
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
                #         #####                    ####  ##
                #         #########                      #
                #
                #
                #                          ##
                #                          ##
                #                          ##
                #                          ##
                #                          ##
                # 
                #                  
                #                 
                #        p        
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                ######################  #################    ###################################
                """, 8);

        environment
                .enableAllFeatures()
                .addSystem(new MovementControlSystem())
                .addSystem(new RenderBackgroundSystem())
                .addSystem(new GrabSystem())
                .addSystem(new WallJumpControlSystem())
                .addSystem(new ClimbSystem())
                .addSystem(new QuitEngineSystem())
                .addSystem(new AirFrictionSystem())
                .addSystem(new JumpControlSystem())
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
