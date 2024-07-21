package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class SpawnModeTest {

    @ParameterizedTest
    @CsvSource({
            "AREA,40,40,20,20",
            "POSITION,50,50,0,0",
            "LEFT_SIDE,40,40,0,20",
            "RIGHT_SIDE,60,40,0,20",
            "TOP_SIDE,40,40,20,0",
            "BOTTOM_SIDE,40,60,20,0"})
    void spawnArea_isSameAsInput(SpawnMode mode, double x, double y, double width, double height) {
        assertThat(mode.spawnArea($$(40, 40, 20, 20))).isEqualTo($$(x, y, width, height));
    }

}
