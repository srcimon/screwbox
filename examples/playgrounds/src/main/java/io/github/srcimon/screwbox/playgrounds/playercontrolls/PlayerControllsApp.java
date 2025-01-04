package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.Player;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Floor;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.WorldRenderSystem;

//TODO add playgrounds to readme.md
public class PlayerControllsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controlls");

        screwBox.graphics().camera().setZoom(3);

        screwBox.environment()
                .enableAllFeatures()
                .addSystem(new WorldRenderSystem())
                .addEntity("gravity", new GravityComponent(Vector.y(700)))
                .importAsciiSource("""
                        #
                        #
                        #
                        #............##....##
                        #..p.##......##....##
                        #######......############
                        """, 8)
                .when('p').as(new Player())
                .when('#').as(new Floor());

        screwBox.start();
    }
}