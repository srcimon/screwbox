package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MaskImageFilterTest {

    @ParameterizedTest
    @CsvSource({
            "10,0,0,#00000000",
            "50,0,0,#00000000",
            "10,10,10,#ffffffff",
            "255,10,10,#00000000",
    })
    void applyFilter_varyingThreshold_filtersImage(int threshold, int x, int y, String resultHex) {
        var image = SpriteBundle.BOX_STRIPED.get().singleImage();
        var mask = SpriteBundle.CLOUDS.get().singleFrame();
        var filter = new MaskImageFilter(mask, threshold);
        var result = ImageOperations.applyFilter(image, filter);

        var resultFrame = Frame.fromImage(result);
        assertThat(resultFrame.colorAt(x, y)).isEqualTo(Color.hex(resultHex));
    }

    @Test
    void applyFilter_alreadyTransparent_doesntAddTransparency() {
        var image = Sprite.pixel(Color.rgb(10, 40, 10, Percent.of(0.2))).singleImage();
        var mask = Sprite.pixel(Color.WHITE).singleFrame();

        var filter = new MaskImageFilter(mask, 0);
        var result = ImageOperations.applyFilter(image, filter);

        var resultFrame = Frame.fromImage(result);
        assertThat(resultFrame.colorAt(0, 0).opacity()).isEqualTo(Percent.of(0.2));
    }
}
