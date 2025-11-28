package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RopeComponentTest {
//TODO Implement list seralization for all objects
    @Test
    void testSerialization() {
        var rope = new RopeComponent();
        rope.nodes.add(new Entity(1));

        var afterRoundTrip = TestUtil.roundTripSerialization(rope);

        assertThat(afterRoundTrip.nodes).hasSize(1);
        assertThat(afterRoundTrip.nodes.getFirst().id()).contains(1);
    }
}
