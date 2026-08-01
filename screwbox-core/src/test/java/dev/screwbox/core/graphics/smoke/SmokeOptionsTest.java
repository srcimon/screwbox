package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import org.junit.jupiter.api.Test;

class SmokeOptionsTest {

    @Test
    void newInstance_allFieldsCustomized_hasCorrectValues() {
        SmokeOptions.noFade()
            .baseVelocity(Vector.y(-2))
            .baseVelocityAdaption(Percent.half())
            .diffusion(0.00004)
            .viscosity(0.001)
            .iterations(3);
        //TODO finish up
    }
}
