package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

//TODO document
//TODO refactor
//TODO test
public record GridAnimation(int gridSize, boolean isLeftToRight) implements TransitionAnimation {

    public GridAnimation {
        Validate.range(gridSize, 24, 480, "grid size must in range 24 to 480");
    }

    public GridAnimation() {
        this(32, true);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, Percent progress) {
        for (int y = 0; y < size.height(); y += gridSize) {
            if (isLeftToRight) {
                for (int x = 0; x < size.width(); x += gridSize) {
                    extracted(source, target, size, progress, x, y);
                }
            } else {
                for (int x = size.width(); x >= 0; x -= gridSize) {
                    extracted(source, target, size, progress, x, y);
                }
            }
        }


    }

    private void extracted(Image source, Graphics2D target, Size size, Percent progress, int x, int y) {
        double blockDelay = ((double) x / size.width() * 0.4) + ((double) y / size.height() * 0.3);
        double localProgress = Math.clamp((progress.value() - blockDelay) / 0.3, 0, 1);

        if (localProgress < 1.0) {

            // 1. Digitaler Versatz (Glitch)
            // Horizontale Linien springen nach links/rechts, vertikale nach oben/unten
            int offsetX = (y % (gridSize * 2) == 0) ? (int) (localProgress * 50) : (int) (-localProgress * 50);
            int offsetY = (x % (gridSize * 2) == 0) ? (int) (localProgress * 30) : (int) (-localProgress * 30);

            // 2. Skalierung: Blöcke schrumpfen zu digitalen Linien
            int drawW = (int) (gridSize * (1.0 - localProgress));
            int drawH = (int) (gridSize * (1.0 - localProgress * 0.5)); // Behält vertikale Linie länger

            // 3. Farbe & Transparenz
            float alpha = (float) (1.0 - localProgress);

            // Wir "schneiden" den Block aus dem Original
            target.setClip(x, y, gridSize, gridSize);
            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Zeichne den Block an der verschobenen Position
            target.drawImage(source,
                x + offsetX, y + offsetY,
                x + offsetX + drawW, y + offsetY + drawH,
                x, y, x + gridSize, y + gridSize,
                null);
        }
    }
}
