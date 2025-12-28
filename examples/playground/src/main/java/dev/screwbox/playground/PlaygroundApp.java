package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.blueprints.Camera;
import dev.screwbox.playground.blueprints.Earth;
import dev.screwbox.playground.blueprints.HangingRope;
import dev.screwbox.playground.blueprints.Water;
import dev.screwbox.playground.builder.BuilderSystem;
import dev.screwbox.playground.misc.DebugJointsSystem;
import dev.screwbox.playground.misc.PhysicsInteractionSystem;

import static dev.screwbox.core.environment.imports.ImportJob.indexedSource;

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
                .addEntity(new Entity().add(new GravityComponent(Vector.y(400))))
                .addSystem(new LogFpsSystem())

                .runImport(indexedSource(map.tiles(), TileMap.Tile::value)
                        .assign('C', new Camera())
                        .assign('#', new Earth())
                        .assign('X', new HangingRope()))

                .runImport(indexedSource(map.blocks(), TileMap.Block::value)
                        .assign('W', new Water()));

        engine.environment().addEntity(new Entity()
                .bounds(Bounds.atOrigin(0, 0, 16, 16))
                .add(new CursorAttachmentComponent())
                .add(new TailwindComponent(40, Percent.max())));

        engine.start();
    }

}