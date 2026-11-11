package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.OrthographicWallComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.light.ShadowOptions;
import dev.screwbox.core.keyboard.DefaultControlSet;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.debug.DebugGridSystem;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            #######################
            #DDDDDDDD###DDDDDDDDDD#
            #DDDDDDDD###DDDDDDDDDD#
            #        ###          #
            #        #DD          #
            #        #DD          #
            #        #            #
            #        D    C    B  #
            #        D            #
            #                B    #
            #      L         BBBB #
            #                     #
            #   ####   P  C       #
            #   DDDD              #
            #   DDDD              #
            #                     #
            #   C              L  #
            #               #     #
            #    C          D     #
            #         ###   D     #
            #         DDD         #
            #         DDD         #
            #                     #
            #                     #
            #######################
            """);

        screwBox.environment().addSystem(new LogFpsSystem());
        screwBox.keyboard().bindAlias(DefaultControlSet.LEFT, Key.A)
            .bindAlias(DefaultControlSet.RIGHT, Key.D)
            .bindAlias(DefaultControlSet.UP, Key.W)
            .bindAlias(DefaultControlSet.DOWN, Key.S);
        screwBox.graphics().configuration().setLightQuality(Percent.half()).setLightmapBlur(4);
        screwBox.graphics().camera().setZoom(3);
        screwBox.environment().addEntity(new Entity()
            .add(new CursorAttachmentComponent())
            .add(new GlowComponent(40, Color.WHITE.opacity(0.2)))
            .add(new TransformComponent())
            .add(new PointLightComponent(80)));
        screwBox.graphics().light().setAmbientLight(Percent.of(0.1));
        screwBox.environment().enableAllFeatures()
            .addSystem(new JumpSystem())
            .addSystem(new CollectableSystem())
            .addSystem(new DebugGridSystem(16, 256))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('C', source -> new Entity().bounds(source.bounds().scale(0.25).moveBy(Vector.random(4, 10)))
                    .add(new RenderComponent(SpriteBundle.DOT_YELLOW.get().scaled(0.25)))
                    .add(new CollectableComponent())
                    .add(new PhysicsComponent(), p -> p.ignoreCollisions = true)
                ).repeatLastAssignment(10)
                .assign('#', source -> new Entity().bounds(source.bounds())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(Sprite.placeholder(Color.hex("#884410"), Size.square(16)))))
                .assign('D', source -> new Entity().bounds(source.bounds())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new OrthographicWallComponent())
                    .add(new RenderComponent(Sprite.placeholder(Color.hex("#af5d1e"), Size.square(16)))))
                .assign('B', source -> new Entity().bounds(source.bounds())
                    .add(new RenderComponent(Sprite.placeholder(Color.BLACK, Size.square(16)))))
                .assign('x', source -> new Entity().bounds(source.bounds())
                    .add(new PointLightComponent(100, Color.BLACK)))
                .assign('P', source -> new Entity(1).bounds(source.bounds().scale(0.75))
                    .add(new PhysicsComponent(), config -> config.friction = 8)
                    .add(new CameraTargetComponent(60))

                    .add(new CameraLeadComponent(), config -> {
                        config.adjustmentSpeed = 0.4;
                        config.xModifier = 0.3;
                        config.yModifier = 0.3;
                    })
                    .add(new BackdropOccluderComponent(ShadowOptions.angular().distortion(Percent.of(0.0)).backdropDistance(0.05)))
                    .add(new MovementControlComponent(), config -> {
                        config.acceleration = 800;
                        config.maxSpeed = 100;
                    })
                )
                .assign('P', source -> new Entity(2)
                    .bounds(source.bounds().scale(0.75))
                    .add(new AttachmentComponent(1))

                    .add(new RenderComponent(Sprite.placeholder(Color.RED, Size.square(12)))))
            );
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}