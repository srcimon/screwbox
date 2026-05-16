package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

class MaxAlphaCompositeTest {

    @Test
    void testCompositeContext() {
        var image = ImageOperations.toBufferedImage(SpriteBundle.SMOKE.get().singleImage());
        var graphics = image.createGraphics();
        graphics.setComposite(MaxAlphaComposite.INSTANCE);

        graphics.drawImage(SpriteBundle.DOT_WHITE.get().singleImage(), 0, 0, null);

        TestUtil.verifyIsSameImage(image, "MaxAlphaComposite.png");
    }
}
