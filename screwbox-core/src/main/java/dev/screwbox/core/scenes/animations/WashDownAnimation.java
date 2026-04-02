package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

//TODO document
//TODO refactor
public record WashDownAnimation(int stripeWidth) implements TransitionAnimation {

    public WashDownAnimation() {
        this(2);
    }

    public WashDownAnimation {
        Validate.range(stripeWidth, 1, 32, "stripe width must be in range 1 to 32");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        // Wir teilen das Bild in schmale vertikale Streifen
        for (int x = 0; x < size.width(); x += stripeWidth) {

            // Individuelle Fall-Geschwindigkeit pro Streifen durch Sinus-Interferenz
            // Erzeugt einen organischen, unregelmäßigen unteren Rand
            double variation = Math.sin(x * 0.05) * Math.cos(x * 0.02) * 0.5 + 0.5;

            // Progress für diesen spezifischen Streifen (verzögert durch Variation)
            double localProgress = Math.max(0, (progress.value() - variation * 0.3) / 0.7);

            if (localProgress < 1.0) {
                // Die vertikale Verschiebung (Beschleunigtes Fallen wie Wasser)
                int offsetY = (int) (Math.pow(localProgress, 2) * size.height());

                // Ein zusätzlicher Wellen-Effekt (horizontaler Schlenker beim Fallen)
                int offsetX = (int) (Math.sin(localProgress * 10 + x) * 5 * localProgress);


                // Wir schneiden den Streifen aus
                target.setClip(x, 0, stripeWidth, size.height());

                // Zeichne den Streifen nach unten verschoben
                target.drawImage(source, offsetX, offsetY, null);

            }
        }
    }
}
