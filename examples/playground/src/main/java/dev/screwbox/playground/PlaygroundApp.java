package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyBoundaryComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyOccluderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.light.ShadowOptions;
import dev.screwbox.core.graphics.options.TextureLineDrawOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.mouse.MouseButton;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundApp {

    static List<Vector> vectors = new ArrayList<>();
    static Angle angle = Angle.degrees(50);

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############################
            ############################
            ##                        ##
            ##                        ##
            ##            ##          ##
            ##            #           ##
            ##                        ##
            ##                        ##
            ##            T           ##
            ##                        ##
            ##                        ##
            ############################
            ############################
            """);

        //TODO SoftBodyRenderComponent.surfaceTexture
        screwBox.graphics().camera().setZoomRestriction(1, 10).setZoom(1);
        screwBox.graphics().configuration()
            .setIndirectLightRaySpacing(0.4)
            .setLightQuality(Percent.of(0.2))
            .setIndirectLightDiameter(16f)
            .setSmokeCellSize(2)
            .setSmokeCellPadding(64)
            .setBackgroundColor(Color.BLUE);
        screwBox.graphics().camera().setPosition(map.bounds().position());
        Sprite hairs = Sprite.animatedFromFile("hairs.png", Size.of(64, 16), Duration.ofMillis(150));
        Sprite sprite = Sprite.fromFile("laser.png");
        screwBox.graphics().enableSplitScreenMode(SplitScreenOptions.viewports(2));
        screwBox.environment().addEntity(new Entity().add(new GravityComponent(Vector.y(400))));
        screwBox.environment().addSystem(Order.PRESENTATION_FOREGROUND, s -> {
            s.environment().fetchAll(Archetype.ofSpacial(SoftBodyComponent.class)).forEach(body -> {
                body.get(SoftBodyComponent.class).shape.segments().forEach(segment -> {

                    s.graphics().world().drawLine(segment, hairs, new TextureLineDrawOptions(1,0.5, null, Percent.max()));

                });

            });
            if (s.mouse().isPressedLeft()) {
                vectors.add(s.mouse().position());
            }
            if(s.keyboard().isPressed(Key.SPACE)) {
                s.audio().playMumble("jum", Voice.variable().waveType(Voice.WaveType.TRIANGLE));
            }
            if (s.mouse().isPressedRight()) {
                var body = SoftPhysicsSupport.createStabilizedSoftBody(Polygon.ofNodes(vectors), s.environment());
                s.environment().addEntities(body);
                body.root().add(new SoftBodyRenderComponent(Color.BLACK), b -> b.rounded = false)
                    .add(new SoftBodyCollisionComponent())
                    .add(new SoftBodyOccluderComponent(ShadowOptions.rounded()))
                    .add(new SoftBodyShapeComponent())
                    .add(new SoftBodyBoundaryComponent());
                body.forEach(b -> b.get(PhysicsComponent.class).friction = 1);
                body.forEach(b -> b.add(new JumpControlComponent()));
                body.forEach(b -> b.add(new LeftRightControlComponent()));
                var collectd = new ArrayList<Entity>();
                body.forEach(node -> {
                    var arm = SoftPhysicsSupport.createRope(node.position(), node.position().addX(20), 4, s.environment());
                    arm.root().add(new AttachmentComponent(node.forceId()));
                    arm.forEach(e -> e.get(PhysicsComponent.class).friction = 1.0);
                    collectd.addAll(arm);
                });

                s.environment().addEntities(collectd);
                vectors.clear();
            }
            angle = angle.addDegrees(s.loop().delta() * 40);
            Vector delta = angle.rotateAroundCenter(s.mouse().position(), Line.normal(s.mouse().position(), 200).end());
            var target = s.navigation().raycastFrom(s.mouse().position()).castingTo(delta).nearestHit().orElse(delta);
            s.graphics().world().drawLine(s.mouse().position(), target, sprite, new TextureLineDrawOptions(1, 1, ShaderBundle.CONVEYOR.get(), Percent.max()));
        });
        screwBox.graphics().light().setAmbientLight(Percent.of(0.5));
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> {
                if (e.mouse().isDown(MouseButton.MIDDLE)) {
                    e.graphics().viewportAt(e.mouse().offset()).camera().move(e.mouse().drag());
                }
                e.graphics().viewportAt(e.mouse().offset()).camera().changeZoomBy(e.mouse().unitsScrolled() / -20.0);
            })
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assign('#', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new BoidObstacleComponent())
                    .add(new SmokeObstacleComponent())
                    .add(new ColliderComponent())
                    .add(new StaticBoundsComponent())
                    .add(new OccluderComponent())
                    .add(new RenderComponent(AutoTileBundle.ROCKS.get().findSprite(source.autoTileMask()))))
                .assign('T', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new CursorAttachmentComponent())
                    .add(new PointLightComponent(120))
                ));

        screwBox.start();
    }
}