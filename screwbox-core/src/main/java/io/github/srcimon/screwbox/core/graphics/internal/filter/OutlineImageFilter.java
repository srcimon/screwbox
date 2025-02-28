package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;

import java.awt.image.RGBImageFilter;

import static io.github.srcimon.screwbox.core.Bounds.$$;

public class OutlineImageFilter extends RGBImageFilter {

    private final Grid grid;
    private final int colorRgb;

    public OutlineImageFilter(final Frame source, final Color color) {
        grid = new Grid($$(0, 0, source.width(), source.height()), 1);
        colorRgb = AwtMapper.toAwtColor(color).getRGB();//TODO short for this!!!

        for (var pixel : source.size().allPixels()) {
            if (!source.colorAt(pixel).equals(Color.TRANSPARENT)) {
                grid.block(pixel.x(), pixel.y());
            }
        }
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb == 0 && grid.blockedNeighbors(grid.nodeAt(x, y)).size() > 0 ? colorRgb : rgb;
    }

}
