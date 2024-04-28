package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EnvironmentExtension.class)
class ReflectionRenderSystemTest {

    @Test
    void update_deletesOldReflectionResults(DefaultEnvironment environment) {
        environment
                .addEntity(1, new ReflectionResultComponent())
                .addSystem(new ReflectionRenderSystem());

        environment.update();

        assertThat(environment.entityCount()).isZero();
    }
}
