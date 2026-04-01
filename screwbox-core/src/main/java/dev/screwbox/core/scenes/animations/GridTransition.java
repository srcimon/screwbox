package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

public class GridTransition implements TransitionAnimation {

    private static final int GRID_SIZE = 32; // Größe der Datenblöcke

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context, Percent progress) {
        final var bounds = context.bounds();
        final double p = progress.value();

        final int width = bounds.width();
        final int height = bounds.height();

        for (int y = 0; y < height; y += GRID_SIZE) {
            for (int x = 0; x < width; x += GRID_SIZE) {

                // Einzigartiger Delay pro Block für das "Defragmentierungs"-Gefühl
                // Nutzt X und Y für ein diagonales/schachbrettartiges Muster
                double blockDelay = ((double) x / width * 0.4) + ((double) y / height * 0.3);
                double localP = Math.max(0, Math.min(1, (p - blockDelay) / 0.3));

                if (localP >= 1.0) continue;

                // 1. Digitaler Versatz (Glitch)
                // Horizontale Linien springen nach links/rechts, vertikale nach oben/unten
                int offsetX = (y % (GRID_SIZE * 2) == 0) ? (int) (localP * 50) : (int) (-localP * 50);
                int offsetY = (x % (GRID_SIZE * 2) == 0) ? (int) (localP * 30) : (int) (-localP * 30);

                // 2. Skalierung: Blöcke schrumpfen zu digitalen Linien
                int drawW = (int) (GRID_SIZE * (1.0 - localP));
                int drawH = (int) (GRID_SIZE * (1.0 - localP * 0.5)); // Behält vertikale Linie länger

                // 3. Farbe & Transparenz
                float alpha = (float) (1.0 - localP);

                final Shape oldClip = target.getClip();
                final Composite oldComposite = target.getComposite();

                // Wir "schneiden" den Block aus dem Original
                target.setClip(x, y, GRID_SIZE, GRID_SIZE);
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                // Zeichne den Block an der verschobenen Position
                target.drawImage(source,
                    bounds.x() + x + offsetX, bounds.y() + y + offsetY,
                    bounds.x() + x + offsetX + drawW, bounds.y() + y + offsetY + drawH,
                    x, y, x + GRID_SIZE, y + GRID_SIZE,
                    null);

                // Optional: Zeichne digitale Gitterlinien (Grid-Overlay)

                target.setComposite(oldComposite);
                target.setClip(oldClip);
            }
        }
    }
}
