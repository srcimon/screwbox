package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.controlls.PlayerControlls;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.states.StandingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.world.WorldRenderSystem;

//TODO add playgrounds to readme.md
public class PlayerControllsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controlls");

        screwBox.keyboard().bindKey(PlayerControlls.JUMP, Key.SPACE);

        screwBox.graphics().camera().setZoom(3);
        screwBox.environment()
                .enableAllFeatures()
                .addSystem(new WorldRenderSystem())
                .addEntity("gravity",
                        new GravityComponent(Vector.y(700)))
                .addEntity("player",
                        new TransformComponent(0, 0, 10, 18),
                        new PhysicsComponent(),
                        new CameraTargetComponent(),
                        new CollisionSensorComponent(),
                        new CollisionDetailsComponent(),
                        new StateComponent(new StandingState()))
                .addEntity("floor",
                        new TransformComponent(-150, 24, 600, 8),
                        new ColliderComponent());

        screwBox.start();
    }
}