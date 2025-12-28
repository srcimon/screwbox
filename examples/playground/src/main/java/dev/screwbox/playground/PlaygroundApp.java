package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.imports.ImportProfile;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.blueprints.Camera;
import dev.screwbox.playground.blueprints.Cursor;
import dev.screwbox.playground.blueprints.Earth;
import dev.screwbox.playground.blueprints.Gravity;
import dev.screwbox.playground.blueprints.HangingRope;
import dev.screwbox.playground.blueprints.Water;
import dev.screwbox.playground.builder.BuilderSystem;
import dev.screwbox.playground.misc.DebugJointsSystem;
import dev.screwbox.playground.misc.PhysicsInteractionSystem;

import static dev.screwbox.core.environment.imports.ImportProfile.globalOnce;
import static dev.screwbox.core.environment.imports.ImportProfile.indexedSource;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);
        var map = TileMap.fromString("""
                
                
                
                      N          # X#
                     ##          ####
                
                                C
                
                
                #######
                ###   ####
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                WWWWWWWWWWWWWWWWWWWWWWWWWW
                """);

        engine.environment()
                .enableAllFeatures()
                .addSystem(new DebugJointsSystem())
                .addSystem(new BuilderSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addSystem(new LogFpsSystem())

                .runImport(new Gravity(), new Cursor())

                .runImport(indexedSource(map.tiles(), TileMap.Tile::value)
                        .assign('C', new Camera())
                        .assign('#', new Earth())
                        .assign('X', new HangingRope()))

                .runImport(indexedSource(map.blocks(), TileMap.Block::value)
                        .assign('W', new Water()));

        engine.start();
    }

}