package dev.screwbox.core.graphics.filter;

import dev.screwbox.core.graphics.ScreenBounds;

import java.awt.*;
import java.awt.image.VolatileImage;

//TODO add split screen support
public class WavePostFilter implements PostProcessingFilter {

    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final ScreenBounds filterArea, final PostProcessingContext context) {
        int w = source.getWidth();
        int h = source.getHeight();

        double time = context.runtime().milliseconds() / 500.0; // Geschwindigkeit
        double waveIntensity = 20.0; // Wie weit schlägt der Wobble aus (Pixel)
        double frequency = 0.05;     // Wie eng liegen die Wellen beieinander

        int rowHeight = 4;
        for (int y = 0; y < h; y += rowHeight) {
            // Berechne den Versatz für diese spezifische Zeile
            int offsetX = (int) (Math.sin((y * frequency) + time) * waveIntensity);

            // Zeichne einen 1-Pixel hohen Streifen des Bildes
            // drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
            target.drawImage(source,
                offsetX, y, w + offsetX, y + rowHeight, // Ziel-Position (auf dem Canvas)
                0, y, w, y + rowHeight,                 // Quell-Bereich (vom Buffer)
                null);
        }
    }
}
