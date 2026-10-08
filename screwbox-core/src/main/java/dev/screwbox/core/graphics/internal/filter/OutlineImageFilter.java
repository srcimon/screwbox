package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;

import java.awt.image.RGBImageFilter;

public class OutlineImageFilter extends RGBImageFilter {

    private final boolean[][] blocked;
    private final int colorRgb;

    public OutlineImageFilter(final Frame source, final Color color) {
        final int width = source.width();
        final int height = source.height();
        blocked = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (source.colorAt(x, y).opacity().hasValue()) {
                    blockNeighbours(x, y, width, height);
                }
            }
        }
        colorRgb = color.rgb();
    }

    private void blockNeighbours(final int x, final int y, final int width, final int height) {
        for (int rx = Math.max(0, x - 1); rx < Math.min(width, x + 2); rx++) {
            for (int ry = Math.max(0, y - 1); ry < Math.min(height, y + 2); ry++) {
                blocked[rx][ry] = true;
            }
        }
    }


    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb == 0 && blocked[x][y] ? colorRgb : rgb;
    }

}
