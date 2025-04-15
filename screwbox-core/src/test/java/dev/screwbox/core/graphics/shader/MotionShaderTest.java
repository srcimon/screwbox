package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThatNoException;

class MotionShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_halfProcessed_appliesPixelRelocation() {
        var result = new MotionShader(1, 2).apply(source, Percent.half());
        TestUtil.verifyIsSameImage(result, "shader/apply_halfProcessed_appliesPixelRelocation.png");
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,0",
            "2,-5,0.5",
            "8,2,1",
            "1,9,0.2",
    })
    void apply_possibleSpeeds_neverOutOfBounds(int x, int y, double progress) {
        assertThatNoException().isThrownBy(() -> new MotionShader(x, y).apply(source, Percent.of(progress)));
    }

}
