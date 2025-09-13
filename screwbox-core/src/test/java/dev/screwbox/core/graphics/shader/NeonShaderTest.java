package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class NeonShaderTest {

    @Test
    void apply_yellow_createsNeonEffect() {
        System.setProperty("sun.java2d.opengl", "true");
        Image image = SpriteBundle.ACHIEVEMENT.get().singleImage();

        var result = new NeonShader(Color.WHITE, Color.YELLOW).apply(image, Percent.half());

        // Metal rendering will create slightly different image
        var distinctPixelCount = Frame.fromImage(result)
                .listPixelDifferences(Frame.fromFile("shader/apply_yellow_createsNeonEffect.png")).size();

        var distinctPixelCountMetal = Frame.fromImage(result)
                .listPixelDifferences(Frame.fromFile("shader/apply_yellow_createsNeonEffect_metal.png")).size();

        assertThat(distinctPixelCount == 0 || distinctPixelCountMetal == 0).isTrue();

    }
}
