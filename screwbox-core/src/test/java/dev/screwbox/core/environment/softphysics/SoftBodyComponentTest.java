package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoftBodyComponentTest {

    @Test
    void testSerialization() {
        var body = new SoftBodyComponent();
        body.nodes.add(new Entity(1));

        var afterRoundTrip = TestUtil.roundTripSerialization(body);

        assertThat(afterRoundTrip.nodes).hasSize(1);
        assertThat(afterRoundTrip.nodes.getFirst().id()).contains(1);
    }
}
