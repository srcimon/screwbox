package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.Player;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.CameraBounds;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Floor;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Gravity;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.WorldRenderSystem;

//TODO add playgrounds to readme.md
public class PlayerControlsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controls");

        screwBox.graphics().camera().setZoom(3);

        var map = AsciiMap.fromString("""
                #         ##        ##
                #         ##        ##
                #         ##        ##
                #
                #
                #                        ##    ##
                #  P ##                  ##    ##
                #######                  ############
                """, 8);

        screwBox.environment()
                .enableAllFeatures()
                .addSystem(new JumpSystem())
                .addSystem(new WorldRenderSystem());

        screwBox.environment().importSource(map)
                .as(new CameraBounds())
                .as(new Gravity());

        screwBox.environment()
                .importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('P').as(new Player())
                .when('#').as(new Floor());

        screwBox.start();
    }
}