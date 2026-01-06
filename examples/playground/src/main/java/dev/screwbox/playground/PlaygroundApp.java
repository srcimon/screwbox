package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.playground.mesh.SoftBodyMeshComponent;
import dev.screwbox.playground.mesh.SoftBodyMeshSystem;
import dev.screwbox.playground.misc.InteractionSystem;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    //TODO UniformMeshShader, ReferenceMeshShader, MeshComponent (calculates structure triangle mesh)
    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new ClothRenderSystem())
            .addSystem(new SoftBodyMeshSystem())
            .addSystem(new InteractionSystem())
//            .addSystem(new DebugSoftPhysicsSystem())
//            .addEntity(new GravityComponent($(-400, 600)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {

                if (e.keyboard().isPressed(Key.ENTER)) {
                    int size = 8;
                    List<Entity> cloth = ClothPrototype.createCloth(Bounds.atOrigin(e.mouse().position(), 64, 64), Size.of(8, 8), e.environment());
                    cloth.getFirst().add(new CursorAttachmentComponent($(0, 32)));
                    cloth.getFirst().add(new SoftBodyMeshComponent());
                    cloth.getFirst().add(new SoftBodyRenderComponent(Color.TRANSPARENT), render -> {
                            render.outlineStrokeWidth = 4;
                            render.outlineColor = Color.WHITE;
                    });
                    cloth.get(64 / size / 4 - 1).add(new CursorAttachmentComponent($(0, 16)));
                    cloth.get(64 / size / 2 - 1).add(new CursorAttachmentComponent($(0, 0)));
                    cloth.get(64 / size / 4 - 1 + 64 / size / 2 - 1).add(new CursorAttachmentComponent($(0, -16)));
                    cloth.get(64 / size - 1).add(new CursorAttachmentComponent($(0, -32)));
                    cloth.forEach(x -> x.add(new ChaoticMovementComponent(80, Duration.ofMillis(250))));
                    cloth.forEach(x -> x.get(PhysicsComponent.class).gravityModifier = 0.3);
                    cloth.forEach(x -> x.get(PhysicsComponent.class).friction = 1.0);
                    cloth.forEach(x -> x.resize(4, 4));
                    cloth.forEach(x -> {
                        var structure = x.get(SoftStructureComponent.class);
                        if (structure != null) {
                            structure.flexibility = 80;
                            structure.retract = 40;
                        }
                    });
                    e.environment().addEntities(cloth);
                }
            });

        engine.start();
    }

}