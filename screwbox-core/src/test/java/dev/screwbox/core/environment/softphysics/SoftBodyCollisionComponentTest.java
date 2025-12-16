package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoftBodyCollisionComponentTest {

    @Test
    void testSerialization() {
        var sensor = new SoftBodyCollisionComponent();
        sensor.collidedNodes.add(1);
        sensor.collidedSegments.add(2);

        var afterRoundTrip = TestUtil.roundTripSerialization(sensor);

        assertThat(afterRoundTrip.collidedNodes).containsExactly(1);
        assertThat(afterRoundTrip.collidedSegments).containsExactly(2);
    }
}
