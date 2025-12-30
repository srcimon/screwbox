package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.utils.FractalNoise;
import dev.screwbox.core.utils.PerlinNoise;
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

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().camera().setZoom(3);
        var map = TileMap.fromString("""
                                C
                
                
                """);

        engine.environment()
                .enableAllFeatures()
                .addSystem(new DebugJointsSystem())
                .addSystem(s -> {
                    for(var e : s.environment().fetchAllHaving(PhysicsComponent.class)) {
                        PhysicsComponent physicsComponent = e.get(PhysicsComponent.class);
                        double v = 30.0;
                        double milliseconds = engine.loop().runningTime().milliseconds() / 50000.0;
                        physicsComponent.velocity = physicsComponent.velocity.add(
                                PerlinNoise.generatePerlinNoise3d(132123L, e.position().x() / v, e.position().y() / v, milliseconds) * 3,
                                PerlinNoise.generatePerlinNoise3d(234234L, e.position().x() / v, e.position().y() / v, milliseconds) * 3
                        );
                    }
                })
                .addSystem(new BuilderSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addSystem(new LogFpsSystem())

                .importSource(source(map)
                        .make(new Cursor()))

                .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                        .assign('C', new Camera())
                        .assign('#', new Earth())
                        .assignComplex('X', new HangingRope()))

                .importSource(indexedSources(map.blocks(), TileMap.Block::value)
                        .assign('W', new Water()));

        engine.start();
    }


}