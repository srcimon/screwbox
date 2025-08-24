package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.RenderingApi;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.utils.TileMap;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
        engine.graphics().camera().setZoom(3);

        var map = TileMap.fromString("""
                
                
                
                1   4      # X#
                          ####
                        c
                2   3
                #######
                ###   ####
                """);

        engine.environment()
                .enableAllFeatures()
                .addSystem(new DebugJointsSystem())
                .addSystem(new JointsSystem())
                .addSystem(new PhysicsInteractionSystem())
                .addSystem(new LogFpsSystem())
                .addEntity(new Entity().add(new GravityComponent(Vector.y(400))));

        var xEntity = map.tiles().stream().filter(tile -> tile.value().equals('X')).findFirst().orElseThrow();
        double dist = 0;
        int max = 8;
        for (int i = max; i >= 0; i--) {
            Entity add = new Entity(100 + i)
                    .name(i == 0 ? "start" : "node")
                    .add(new FloatComponent())
                    .bounds(xEntity.bounds().moveBy(0, dist).expand(-12))
                    .add(new PhysicsComponent(), p -> p.friction = 80);

            if (i != max) {
                add.add(new JointComponent(List.of(new Joint(100 + i + 1))));
            } else {
                add.add(new JointComponent(new ArrayList<>()));
            }
            engine.environment().addEntity(add);
            dist += 12;
        }

        engine.environment()
                .importSource(map.tiles())
                .usingIndex(TileMap.Tile::value)
                .when('#').as(tile -> new Entity().bounds(tile.bounds())
                        .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                        .add(new ColliderComponent())
                        .add(new StaticColliderComponent()))

                .when('1').as(tile -> new Entity(1).bounds(tile.bounds().expand(-12))
                        .add(new JointComponent(List.of(new Joint(3), new Joint(4))))
                        .add(new PhysicsComponent(), p -> p.friction = 80))

                .when('2').as(tile -> new Entity(2).bounds(tile.bounds().expand(-12))
                        .add(new JointComponent(List.of(new Joint(1), new Joint(3))))
                        .add(new PhysicsComponent(), p -> p.friction = 80))

                .when('3').as(tile -> new Entity(3).bounds(tile.bounds().expand(-12))
                        .add(new JointComponent(List.of(new Joint(4))))
                        .add(new PhysicsComponent(), p -> p.friction = 80))

                .when('4').as(tile -> new Entity(4).bounds(tile.bounds().expand(-12))
                        .add(new JointComponent(List.of(new Joint(2))))
                        .add(new PhysicsComponent(), p -> p.friction = 80))

                .when('5').as(tile -> new Entity(5).bounds(tile.bounds().expand(-12))
                        .add(new JointComponent(List.of(new Joint(1), new Joint(2))))
                        .add(new PhysicsComponent(), p -> p.friction = 80));

        engine.start();
    }
}