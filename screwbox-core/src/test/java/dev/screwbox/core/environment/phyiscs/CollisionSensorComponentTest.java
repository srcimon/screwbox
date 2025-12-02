package dev.screwbox.core.environment.phyiscs;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CollisionSensorComponentTest {

    @Test
    void testSerialization() {
        var sensor = new CollisionSensorComponent();
        sensor.collidedEntities.add(new Entity(1));

        var afterRoundTrip = TestUtil.roundTripSerialization(sensor);

        assertThat(afterRoundTrip.collidedEntities).hasSize(1);
        assertThat(afterRoundTrip.collidedEntities.getFirst().id()).contains(1);
    }
}
