package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.utils.TileMap;

import java.util.Random;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
                                                    ##
               ##   #         C            ##      ####
            ###########WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            #############################################
            #############################################
            #############################################
            """);

        screwBox.graphics().camera().setZoom(3);
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new FluidPostfilterSystem())
            .importSource(indexedSources(map.blocks(), TileMap.Block::value)
                .assign('W', block -> new Entity().name("water")
                    .bounds(block.bounds().expandTop(-8))
                    .add(new FluidComponent((int) (block.bounds().width() / 16)))
                    .add(new BoidObstacleComponent(), config -> config.isContainer = true)
                    .add(new FluidRenderComponent(), config -> {
                        config.surfaceColor = Color.WHITE.opacity(0.5);
                        config.surfaceStrokeWidth = 1;
                    })
                    .add(new FluidTurbulenceComponent(300))
                )
                .assignMultiple('W', 8, block -> new Entity().name("fish")
                    .bounds(Bounds.atPosition(block.bounds().position(), 8, 8))
                    .add(new RenderComponent(SpriteBundle.DOT_WHITE.get().scaled(0.25)))
                    .add(new PhysicsComponent())
                    .add(new BoidComponent(), config -> {
                        config.obstaclePerceptionRadius = 20;
                        config.separationStrength = 4;
                        config.cohesionStrength = 2;
                        config.alignmentStrenth = 9;
                        config.perceptionRadius = 20;
                        config.obstacleAvoidanceStrength = 8;
                        config.velocity = RANDOM.nextDouble(20, 30);
                    })
                )
            )
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity().name("earth")
                    .bounds(tile.bounds())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(tile.autoTileMask())))
                    .add(new StaticColliderComponent())
                    .add(new ColliderComponent())
                )
                .assign('W', tile -> new Entity().name("earth")
                    .bounds(tile.bounds())
                    .add(new RenderComponent(AutoTileBundle.CANDYLAND.get().findSprite(tile.autoTileMask()), SpriteDrawOptions.originalSize().drawOrder(-1)))
                )
                .assign('C', tile -> new Entity().name("camera")
                    .bounds(tile.bounds())
                    .add(new CameraTargetComponent())
                )
            );

        screwBox.start();
    }

}