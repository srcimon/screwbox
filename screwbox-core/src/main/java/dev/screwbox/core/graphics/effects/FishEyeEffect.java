package dev.screwbox.core.graphics.effects;

import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.VolatileImage;

public class FishEyeEffect implements PostProcessingEffect {

    private final int gridSize;
    private final double strength;

    public FishEyeEffect(int gridSize, double strength) {
        Validate.range(gridSize, 2, 128, "grid size must be in range 1 to 128");
        Validate.range(strength, -1, 1, "strenght must be in range -1 to 1");
        this.gridSize = gridSize;
        this.strength = strength;
    }

    public void apply(final VolatileImage source, final Graphics2D graphics) {
        int w = source.getWidth();
        int h = source.getHeight();

        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);
        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {

                //TODO refactor
                // Berechne Distanz zum Zentrum für die Kachel
                double dx = x - centerX;
                double dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;

                double factor = 1.0 + strength * (dist * dist);

                int tx = (int) (centerX + dx * factor);
                int ty = (int) (centerY + dy * factor);
                int tw = (int) (gridSize * factor) + 1; // +1 verhindert feine Lücken
                int th = (int) (gridSize * factor) + 1;

                graphics.drawImage(source,
                    tx, ty, tx + tw, ty + th,
                    x, y, x + gridSize, y + gridSize,
                    null);
            }
        }
    }
}
