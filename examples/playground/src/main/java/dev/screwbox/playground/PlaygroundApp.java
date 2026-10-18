package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ComplexBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.light.BackdropOccluderComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.physics.AttachmentComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeInteractionComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.environment.smoke.WindForceComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyBoundaryComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.light.ShadowOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.styles.HeatVisionSmokeStyle;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        var map = TileMap.fromString("""
            ############################
            ############################
            ##                B       ##
            ##   BB                   ##
            ##   B        ##     B    ##
            ##            #           ##
            ##      B   X   BBB       ##
            ##      T          X      ##
            ##       BBBB X       X   ##
            ##               X        ##
            ##         X              ##
            ##   S         S    S     ##
            ############################
            ############################
            
            
            
            
            """);

        screwBox.graphics().camera().setZoomRestriction(3, 10).setZoom(3);
        screwBox.graphics().configuration()
            .setIndirectLightRaySpacing(0.5)
            .setLightQuality(Percent.of(0.4))
            .setIndirectLightDiameter(6f)
            .setSmokeCellSize(2)
            .setSmokeCellPadding(64)
            .setBackgroundColor(Color.BLUE);

        screwBox.graphics().smoke().setOptions(SmokeOptions.noFade()
            .diffusion(Percent.of(0.000008)).style(new HeatVisionSmokeStyle()).opacity(Percent.of(0.85)));
        screwBox.graphics().light().setAmbientLight(Percent.of(0.5));
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> {
                if(e.mouse().isDownLeft()) {
                    e.graphics().camera().move(e.mouse().drag());
                }
                e.graphics().camera().changeZoomBy(e.mouse().unitsScrolled() / -20.0);
            })
            .addEntity(new Entity().add(new CameraTargetComponent(), s -> {
                s.maxSpeed = 0.25;
                s.allowTeleport=false;
            }).bounds(map.bounds()))
            .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
                .assignComplex('X', (source, idPool) -> {
                    var body =SoftPhysicsSupport.createSoftBody(source.bounds().expand(new Random().nextDouble(-4, 4)), idPool);
                    body.root()
                        .add(new SoftBodyCollisionComponent())
                        .add(new SoftBodyBoundaryComponent())
                        .add(new SoftBodyRenderComponent(Color.random().opacity(0.7)), r -> {
                        r.outlineStrokeWidth = 0.75;
                        r.rounded=false;
                        r.outlineColor=Color.WHITE.opacity(0.5);
                    });
                    body.forEach(b -> b.add(new WindForceComponent()));
                    var all = new ArrayList<Entity>();
                    all.addAll(body);
                    for(var node : body) {
                        var rope = SoftPhysicsSupport.createRope(node.position(), node.position().addX(4), 3, idPool);
                        rope.root().add(new RopeRenderComponent(Color.WHITE, 0.75));
                        rope.root().add(new AttachmentComponent(node.forceId()));
                        rope.forEach(r -> r.get(PhysicsComponent.class).friction = 1);
                        all.addAll(rope);
                    }
                    return all;
                })
                .assign('B', source -> new Entity()
                    .bounds(source.bounds().scale(0.15))
                    .add(new SpotLightComponent(20, Color.BLACK.opacity(0.5)))
                    .add(new GlowComponent(20, Color.YELLOW.opacity(0.2)))
                    .add(new BoidComponent(), config -> {
                        config.velocity = 25;
                    })
                    .add(new BackdropOccluderComponent(ShadowOptions.angular().backdropDistance(0.2)))
                    .add(new PhysicsComponent())
                    .add(new RenderComponent(Sprite.pixel(Color.YELLOW)))
                    .add(new SmokeInteractionComponent()))
                .repeatLastAssignment(1)
                .assign('S', source -> new Entity()
                    .bounds(source.bounds().moveBy(Vector.y(source.bounds().height() / 2 - 1)))
                    .add(new SmokeEmitterComponent(65, Color.WHITE, Vector.y(-500))))
                .assign('S', source -> new Entity()
                    .bounds(source.bounds())
                    .add(new WindComponent(Vector.y(-20))))
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