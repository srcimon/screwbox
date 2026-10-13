package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.test.TestSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CollisionSensorComponentTest {

    @Test
    void testSerialization() {
        var sensor = new CollisionSensorComponent();
        sensor.collidedEntities.add(new Entity(1));

        var afterRoundTrip = TestSupport.roundTripSerialization(sensor);

        assertThat(afterRoundTrip.collidedEntities).hasSize(1);
        assertThat(afterRoundTrip.collidedEntities.getFirst().id()).contains(1);
    }
}
