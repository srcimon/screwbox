package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.DiveComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.fluids.FloatRotationComponent;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidEffectsComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.physics.AirFrictionComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.AsciiMap;

public class PlaygroundScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.graphics().camera().setZoom(3);
    }

    @Override
    public void populate(Environment environment) {
        var map = AsciiMap.fromString("""
                
                                                           P
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
                """);

        environment.importSource(map)
                .as(world -> new Entity().name("world-info")
                        .add(new CameraBoundsComponent(world.bounds()))
                        .add(new GravityComponent(Vector.y(600))));

        environment.importSource(map.tiles())
                .usingIndex(AsciiMap.Tile::value)

                .when('#').as(tile -> new Entity().name("ground")
                        .bounds(tile.bounds())
                        .add(new RenderComponent(Sprite.placeholder(Color.hex("#144c64"), tile.size())))
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()))

                .when('P').as(tile -> new Entity().name("player")
                        .bounds(tile.bounds())
                        .add(new PhysicsComponent())
                        .add(new FluidInteractionComponent())
                        .add(new CollisionDetailsComponent())
                        .add(new CollisionSensorComponent(), sensor -> sensor.range = 2)
                        .add(new AirFrictionComponent(250, 20))
                        .add(new JumpControlComponent())
                        .add(new SuspendJumpControlComponent(), c -> c.maxJumps = 2)
                        .add(new CameraTargetComponent())
                        .add(new FloatComponent(400, 500))
                        .add(new FloatRotationComponent())
                        .add(new RenderComponent(Sprite.placeholder(Color.RED, tile.size())))
                        .add(new LeftRightControlComponent()));

        environment.importSource(map.blocks())
                .usingIndex(AsciiMap.Block::value)
                .when('w').as(block -> new Entity().name("water")
                        .bounds(block.bounds().expandTop(-8))
                        .add(new FluidTurbulenceComponent())
                        .add(new FluidEffectsComponent())
                        .add(new FluidComponent((int) block.bounds().width() / 8))
                        .add(new FluidRenderComponent()))

                .when('B').as(block -> new Entity().name("box")
                        .bounds(block.bounds())
                        .add(new PhysicsComponent())
                        .add(new DiveComponent(0.5))
                        .add(new FluidInteractionComponent())
                        .add(new FloatRotationComponent())
                        .add(new RenderComponent(Sprite.placeholder(Color.hex("#144c64"), block.size())))
                        .add(new FloatComponent(), config -> config.dive = 0.25)
                        .add(new ColliderComponent()));

        environment
                .enableAllFeatures()
                .addSystem(e -> {
                    e.graphics().enableSplitScreenMode(SplitScreenOptions.viewports(4));
                    e.graphics().world().drawCircle(e.graphics().toWorld(Offset.at(1000,10)), 12, CircleDrawOptions.fading(Color.RED));
//                    e.window().filesDroppedOnWindow().ifPresent(f -> {
//                        System.out.println("DROP");
//                        Vector position = e.graphics().toWorld(f.offset());
//
//                        Entity box = new Entity().name("box")
//                                .bounds(Bounds.atPosition(position, 20, 20))
//                                .add(new PhysicsComponent())
//                                .add(new DiveComponent(0.5))
//                                .add(new FluidInteractionComponent())
//                                .add(new FloatRotationComponent())
//                                .add(new RenderComponent(Sprite.placeholder(Color.hex("#144c64"), Size.square(20))))
//                                .add(new FloatComponent(), config -> config.dive = 0.25)
//                                .add(new ColliderComponent());
//                        e.environment().addEntity(box);
//                        System.out.println(e.environment().entityCount());
//                    });
                })
                .addSystem(new SwitchSceneSystem())
                .addSystem(new LogFpsSystem());
    }
}