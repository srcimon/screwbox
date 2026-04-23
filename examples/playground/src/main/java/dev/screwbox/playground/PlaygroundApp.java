package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidEffectsComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.core.utils.TileMap;

import java.util.Random;

import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
                                                    ##
                    # P                 ##      ####
            WWWWWWW####WWWWWWWWWWWWWWW###################
            WWWWWWW####WWWWWWWWWWWWWWW###################
            WWWWWWW####WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            ###########WWWWWWWWWWWWWWW###################
            #############################################
            #############################################
            #############################################
            """);


        screwBox.loop().unlockFps();
        screwBox.graphics().camera().setZoom(4);
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().name("gravity").add(new GravityComponent(Vector.y(600))))
            .addSystem(new FluidPostfilterSystem())
            .importSource(indexedSources(map.blocks(), TileMap.Block::value)
                .assign('W', block -> new Entity().name("water")
                    .bounds(block.bounds().expandTop(-8))
                    .add(new FluidPostFilterComponent())
                    .add(new FluidComponent((int) (block.bounds().width() / 16)), config -> {
                        config.retract = 80;
                        config.transmission = 50;
                    })
                    .add(new BoidObstacleComponent(), config -> config.isContainer = true)
                    .add(new FluidEffectsComponent(), config -> {
                        config.particleOptions = ParticlesBundle.SMOKE_TRAIL.get();
                        config.scheduler = Scheduler.withInterval(Duration.ofMillis(20));
                    })
                    .add(new FluidRenderComponent(Color.hex("#777fd8").opacity(0.6), Color.hex("#3445ff").opacity(0.7)), config -> {
                        config.surfaceColor = Color.WHITE.opacity(0.5);
                        config.surfaceStrokeWidth = 1;
                    })
                    .add(new FluidTurbulenceComponent(300))
                )
                .assign('W', block -> new Entity().name("fish")
                    .bounds(Bounds.atPosition(block.bounds().position(), 8, 8))
                    .add(new RenderComponent(Sprite.fromFile("fish.png").replaceColor(Color.WHITE, Color.random()), SpriteDrawOptions.scaled(RANDOM.nextDouble(0.3, 0.6))))
                    .add(new MotionRotationComponent())
                    .add(new PhysicsComponent(), config -> config.gravityModifier = 0)
                    .add(new BoidComponent(), config -> {
                        config.obstaclePerceptionRadius = 20;
                        config.separationStrength = 6;
                        config.cohesionStrength = 2;
                        config.alignmentStrenth = 9;
                        config.perceptionRadius = 20;
                        config.obstacleAvoidanceStrength = 8;
                        config.velocity = RANDOM.nextDouble(20, 30);
                    })
                )
                .repeatLastAssignment(19)
            )
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', tile -> new Entity().name("earth")
                    .bounds(tile.bounds())
                    .add(new StaticColliderComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(tile.autoTileMask())))
                    .add(new ColliderComponent(), config -> config.friction = 100)
                )
                .assign('W', tile -> new Entity().name("background")
                    .bounds(tile.bounds())
                    .add(new RenderComponent(Sprite.placeholder(Color.GREY, Size.square(16)), SpriteDrawOptions.originalSize().drawOrder(-1)))
                )
                .assign('P', tile -> new Entity().name("player")
                    .bounds(tile.bounds())
                    .add(new RenderComponent(SpriteBundle.BOX.get().scaled(0.5)))
                    .add(new PhysicsComponent(), config -> config.friction = 0.5)
                    .add(new FluidInteractionComponent(4, 2))
                    .add(new FloatComponent())
                    .add(new LeftRightControlComponent())
                    .add(new JumpControlComponent())
                    .add(new SuspendJumpControlComponent())
                    .add(new CollisionSensorComponent())
                    .add(new CollisionDetailsComponent())
                    .add(new CameraTargetComponent())
                )
            );

        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);

        screwBox.start();
    }

}