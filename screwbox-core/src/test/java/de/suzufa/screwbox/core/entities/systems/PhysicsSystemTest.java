package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.GravityComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class PhysicsSystemTest {

    @Test
    void update_updatesPositionOfPhysicItems(DefaultEntities entities, GameLoop loop) {
        when(loop.delta()).thenReturn(0.5);
        Entity body = new Entity().add(
                new TransformComponent(Bounds.atPosition(0, 0, 10, 10)),
                new PhysicsBodyComponent(Vector.of(4, 4)));

        entities.add(body);
        entities.add(new PhysicsSystem());

        entities.update();

        Vector center = body.get(TransformComponent.class).bounds.position();
        assertThat(center).isEqualTo(Vector.of(2, 2));
    }

    @Test
    void update_physicBodiesCollideWithEnvironment(DefaultEntities entities, GameLoop loop) {
        when(loop.delta()).thenReturn(0.8);

        Entity ball = new Entity().add(
                new TransformComponent(Bounds.atOrigin(50, 0, 20, 20)),
                new PhysicsBodyComponent(),
                new CollisionSensorComponent());

        Entity ground = new Entity().add(
                new TransformComponent(Bounds.atOrigin(0, 200, 140, 40)),
                new ColliderComponent());

        Entity gravity = new Entity().add(new GravityComponent(Vector.of(0, 20)));

        entities.add(ball, ground, gravity);

        entities.add(
                new PhysicsSystem(),
                new GravitySystem(),
                new CollisionSensorSystem());

        entities.updateTimes(6);

        Vector ballPosition = ball.get(TransformComponent.class).bounds.position();
        assertThat(ballPosition).isEqualTo(Vector.of(60, 190));
    }

}
