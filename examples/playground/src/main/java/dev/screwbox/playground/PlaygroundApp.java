package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.controls.MovementControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraLeadComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.light.ShadowOptions;
import dev.screwbox.core.utils.TileMap;
import dev.screwbox.playground.debug.DebugGridSystem;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            #        ###          #
            #        #            #
            #        #            #
            #            C    B   #
            #                B    #
            #      L         BBBB #
            #                     #
            #   ####   P  C       #
            #                     #
            #   C              L  #
            #             #       #
            #    C         #      ##
            #         ###         #
            #######################
            """);

        screwBox.graphics().configuration().setIndirectLightDiameter(32).setIndirectLightRaySpacing(3).setLightBounceIntensityLoss(Percent.of(0.8)).setMaxLightBounces(1);
        screwBox.environment().addSystem(new LogFpsSystem());
        screwBox.graphics().configuration().setLightQuality(Percent.half());
        screwBox.graphics().camera().setZoom(4);
        screwBox.graphics().light().setAmbientLight(Percent.of(0.3));
        screwBox.environment().enableAllFeatures()
            .addSystem(new JumpSystem())
            .addSystem(new CollectableSystem())
            .addSystem(new DebugGridSystem(16, 256))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('C', source -> new Entity().bounds(source.bounds().scale(0.25).moveBy(Vector.random(10)))
                    .add(new RenderComponent(SpriteBundle.DOT_YELLOW.get().scaled(0.25)))
                    .add(new CollectableComponent())
                    .add(new PhysicsComponent(), p -> p.ignoreCollisions = true)
                ).repeatLastAssignment(10)
                .assign('#', source -> new Entity().bounds(source.bounds())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(AutoTileBundle.BUBBLEGUM.get().findSprite(source.autoTileMask()))))
                .assign('B', source -> new Entity().bounds(source.bounds())
                    .add(new RenderComponent(Sprite.placeholder(Color.BLACK, Size.square(16)))))
                .assign('L', source -> new Entity().bounds(source.bounds())
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