package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.utils.AsciiMap;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.Player;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb.ClimbSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb.WallJumpSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.CameraBounds;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Floor;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.Gravity;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.ResetWorldSystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.WorldRenderSystem;

public class GameScene implements Scene {
    @Override
    public void populate(Environment environment) {
        var map = AsciiMap.fromString("""
                #                         ##                ##
                #                         ##                ##
                #                         ##                ##
                #
                #
                #
                #                                              ##    ##
                #  P         ##     ##     ##                  ##    ##
                ###############     ##     ##                  ############
                """, 8);

        environment.enableAllFeatures()
                .addSystem(new JumpSystem())
                .addSystem(new ResetWorldSystem())
                .addSystem(new WallJumpSystem())
                .addSystem(new ClimbSystem())
                .addSystem(new MovementSystem())
                .addSystem(new WorldRenderSystem());

        environment.importSource(map)
                .as(new CameraBounds())
                .as(new Gravity());

        environment.importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)
                .when('P').as(new Player())
                .when('#').as(new Floor());
    }
}
