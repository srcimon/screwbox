package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.ai.BoidSystem;
import dev.screwbox.core.environment.ai.TargetMovementComponent;
import dev.screwbox.core.environment.ai.TargetMovementSystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidInteractionComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.utils.TileMap;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.environment.importing.ImportCondition.sourceMatches;
import static dev.screwbox.core.environment.importing.ImportOptions.indexedSources;

public class PlaygroundApp {
    static int i = 0;

    public static void main(String[] args) {

        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(3);
        screwBox.graphics().camera().move($(40, 20));
        var map = TileMap.fromImageFile("input.png", Size.square(1));
        BoidComponent component = new BoidComponent();
        component.velocity = 40;
        component.alignmentStrenth = 8;
        component.cohesionStrength = 6;
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addEntity(new Entity().bounds(map.bounds().scale(9))
                .add(new BoidObstacleComponent(), c -> c.isContainer = true)
            )
            .importSource(indexedSources(map.tiles(), TileMap.Tile::value)
                .assign(sourceMatches(o -> o.value().isVisible()), source -> new Entity(i++)
                    .add(component)
                    .bounds(source.bounds().scale(4))
                    .add(new FluidInteractionComponent())
                    .add(new RenderComponent(Sprite.pixel(source.value())))
                    .add(new PhysicsComponent())
                    .add(new SpotLightComponent(10, Color.BLACK))
                    .add(new TargetMovementComponent(source.position(), 80))))

            .addEntity(new Entity()
                .bounds(Bounds.atPosition(map.bounds().position().addY(140), 800, 200))
                .add(new FluidRenderComponent())
                .add(new FluidComponent(10))
                .add(new FluidTurbulenceComponent(300)))
            .addSystem(e -> {
                if (e.audio().microphoneLevel().value() < 0.2) {
                    e.environment().remove(TargetMovementSystem.class);
                    e.environment().addOrReplaceSystem(new BoidSystem());
                } else {
                    e.environment().remove(BoidSystem.class);
                    e.environment().addOrReplaceSystem(new TargetMovementSystem());
                }
            });
        System.out.println(i);
        screwBox.start();
    }

}