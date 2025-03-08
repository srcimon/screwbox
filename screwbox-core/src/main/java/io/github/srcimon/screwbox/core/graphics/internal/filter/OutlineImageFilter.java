package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;

import java.awt.image.RGBImageFilter;

public class OutlineImageFilter extends RGBImageFilter {

    private final boolean[][] blocked;
    private final int colorRgb;

    public OutlineImageFilter(final Frame source, final Color color) {
        blocked = new boolean[source.width()][source.height()];
        for (int x = 0; x < source.width(); x++) {
            for (int y = 0; y < source.height(); y++) {
                if (!source.colorAt(x, y).opacity().isZero()) {
                    blockNeighbours(source, x, y);
                }
            }
        }
        colorRgb = AwtMapper.toAwtColor(color).getRGB();
    }

    private void blockNeighbours(final Frame source, final int x, final int y) {
        for (int rx = Math.max(0, x - 1); rx < Math.min(source.width(), x + 2); rx++) {
            for (int ry = Math.max(0, y - 1); ry < Math.min(source.height(), y + 2); ry++) {
                blocked[rx][ry] = true;
            }
        }
    }


    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb == 0 && blocked[x][y] ? colorRgb : rgb;
    }

}
