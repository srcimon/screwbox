package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.AsciiMap;
import io.github.srcimon.screwbox.core.environment.AsciiTile;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.Player;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.CameraBounds;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Floor;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Gravity;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.WorldRenderSystem;

//TODO add playgrounds to readme.md
public class PlayerControllsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controlls");

        screwBox.graphics().camera().setZoom(3);

        var map = AsciiMap.fromString("""
                #........##.......##
                #........##.......##
                #........##.......##
                #
                #
                #......................##....##
                #..p.##................##....##
                #######................############
                """, 8);

        screwBox.environment()
                .enableAllFeatures()
                .addSystem(new WorldRenderSystem());

        screwBox.environment().importSource(map)
                .as(new CameraBounds())
                .as(new Gravity());

        screwBox.environment()
                .importSource(map.tiles())
                .usingIndex(AsciiTile::value)
                .when('p').as(new Player())
                .when('#').as(new Floor());

        screwBox.start();
    }
}