package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.test.TestSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RopeComponentTest {

    @Test
    void testSerialization() {
        var rope = new RopeComponent();
        rope.isLengthFixed = true;
        rope.nodes.add(new Entity(1));

        var afterRoundTrip = TestSupport.roundTripSerialization(rope);

        assertThat(afterRoundTrip.nodes).hasSize(1);
        assertThat(afterRoundTrip.nodes.getFirst().id()).contains(1);
        assertThat(afterRoundTrip.isLengthFixed).isTrue();
    }
}
