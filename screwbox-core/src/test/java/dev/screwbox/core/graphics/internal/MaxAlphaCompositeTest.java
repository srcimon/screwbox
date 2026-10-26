package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.test.TestSupport;
import org.junit.jupiter.api.Test;

class MaxAlphaCompositeTest {

    @Test
    void testCompositeContext() {
        var image = SpriteBundle.SMOKE.get().singleImage();
        var graphics = image.createGraphics();
        graphics.setComposite(new MaxAlphaComposite());

        graphics.drawImage(SpriteBundle.DOT_WHITE.get().singleImage(), 0, 0, null);

        TestSupport.verifyIsSameImage(image, "MaxAlphaComposite.png");
    }
}
