package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;

public class WaterDropTransitionFilter implements TransitionAnimation {

    private static final int STRIP_WIDTH = 4; // Breite der "Wasserfäden"

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context, Percent progress) {
        final var bounds = context.bounds();
        final double p = progress.value();

        final int width = bounds.width();
        final int height = bounds.height();

        // Wir teilen das Bild in schmale vertikale Streifen
        for (int x = 0; x < width; x += STRIP_WIDTH) {

            // Individuelle Fall-Geschwindigkeit pro Streifen durch Sinus-Interferenz
            // Erzeugt einen organischen, unregelmäßigen unteren Rand
            double variation = Math.sin(x * 0.05) * Math.cos(x * 0.02) * 0.5 + 0.5;

            // Progress für diesen spezifischen Streifen (verzögert durch Variation)
            double localP = Math.max(0, (p - variation * 0.3) / 0.7);

            if (localP >= 1.0) continue; // Streifen ist komplett weggeflossen

            // Die vertikale Verschiebung (Beschleunigtes Fallen wie Wasser)
            int offsetY = (int) (Math.pow(localP, 2) * height);

            // Ein zusätzlicher Wellen-Effekt (horizontaler Schlenker beim Fallen)
            int offsetX = (int) (Math.sin(localP * 10 + x) * 5 * localP);

            final Shape oldClip = target.getClip();
            final Composite oldComposite = target.getComposite();

            // Wir schneiden den Streifen aus
            target.setClip(x, 0, STRIP_WIDTH, height);

            // Zeichne den Streifen nach unten verschoben
            target.drawImage(source,
                bounds.x() + offsetX, bounds.y() + offsetY,
                null);

            target.setComposite(oldComposite);
            target.setClip(oldClip);
        }
    }
}
