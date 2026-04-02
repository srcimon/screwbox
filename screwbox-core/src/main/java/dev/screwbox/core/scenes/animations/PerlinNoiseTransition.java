package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

//TODO rename
//TODO document
//TODO test
//TODO guide
public record PerlinNoiseTransition(long seed, int gridSize) implements TransitionAnimation {

    public PerlinNoiseTransition() {
        this(System.currentTimeMillis(), 40);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        final double p = progress.value();
        final int width = size.width();
        final int height = size.height();

        final AffineTransform canvasTransform = target.getTransform();

        for (int y = 0; y < height; y += gridSize) {
            for (int x = 0; x < width; x += gridSize) {

                double distToCenter = Math.sqrt(Math.pow(x - width / 2.0, 2) + Math.pow(y - height / 2.0, 2)) / width;
                double localP = Math.clamp((p - distToCenter * 0.4 - 0.5 * 0.2) / 0.4, 0, 1);

                if (localP <= 0) {
                    // FIX: Deaktiviere Interpolation für statische Zellen, um "Bleeding" zu verhindern
                    target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                    // FIX: Zeichne 1 Pixel größer (+1), um Haarlinien durch Rundungsfehler zu überdecken
                    target.drawImage(source,
                        x, y, x + gridSize + 1, y + gridSize + 1,
                        x, y, x + gridSize, y + gridSize,
                        null);
                } else if (localP < 0.98) {
                    // Aktiviere Qualität nur für die bewegten/skalierten Zellen

                    double scale = 1.0 - localP;

                    AffineTransform tx = new AffineTransform(canvasTransform);
                    tx.translate(x + gridSize / 2.0, y + gridSize / 2.0);
                    tx.scale(scale, scale);
                    tx.translate(-gridSize / 2.0, -gridSize / 2.0);

                    target.setTransform(tx);

                    target.drawImage(source,
                        0, 0, gridSize, gridSize,
                        x, y, x + gridSize, y + gridSize,
                        null);


                    // Reset für die nächste statische Zelle
                    target.setTransform(canvasTransform);
                }
            }
        }
        target.setTransform(canvasTransform);
    }
}
