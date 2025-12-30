package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.importing.ComplexBlueprint;
import dev.screwbox.core.environment.importing.ImportCondition;
import dev.screwbox.core.environment.importing.ImportContext;
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

import java.util.List;

import static dev.screwbox.core.environment.importing.ImportCondition.lastAssignmentFailed;
import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

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

                .importSource(source(map)
                        .make(new Cursor())
                        .make(new Gravity()))

                .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                        .assign('C', new Camera())
                        .assign('#', new Earth())
                        .assign(lastAssignmentFailed(), new ComplexBlueprint<TileMap.Tile<Character>>() {
                            @Override
                            public List<Entity> assembleFrom(TileMap.Tile<Character> source, ImportContext context) {
                                System.out.println(context.lastAssignmentWasApplied());
                                return List.of();
                            }
                        })
                        .assignComplex('X', new HangingRope()))

                .importSource(indexedSources(map.blocks(), TileMap.Block::value)
                        .assign('W', new Water()));

        engine.start();
    }


}