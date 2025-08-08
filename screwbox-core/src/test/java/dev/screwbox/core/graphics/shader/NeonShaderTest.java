package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

class NeonShaderTest {

    @Test
    void apply_yellow_createsNeonEffect() {
        Image image = SpriteBundle.ACHIEVEMENT.get().singleImage();

        var result = new NeonShader(Color.WHITE, Color.YELLOW).apply(image, Percent.half());

        TestUtil.verifyIsSameImage(result, "shader/apply_yellow_createsNeonEffect.png");
    }
}
