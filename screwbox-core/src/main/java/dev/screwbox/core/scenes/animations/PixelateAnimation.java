package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.awt.geom.AffineTransform;

//TODO rename
//TODO document
//TODO test
//TODO guide
public record PixelateAnimation(int gridSize) implements TransitionAnimation {

    public PixelateAnimation() {
        this(40);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        final double p = progress.value();
        final int width = size.width();
        final int height = size.height();

        final AffineTransform canvasTransform = target.getTransform();

        for (int y = 0; y < height; y += gridSize) {
            for (int x = 0; x < width; x += gridSize) {

                // 1. Noise für die "Unruhe" des Wassers (Z-Achse animiert mit Progress)
                double noise = PerlinNoise.generatePerlinNoise3d(1232343L, x * 0.04, y * 0.04, p * 2);

                // 2. Wellen-Logik: Distanz zum Zentrum für kreisförmige Ausbreitung
                double distToCenter = Math.sqrt(Math.pow(x - width / 2.0, 2) + Math.pow(y - height / 2.0, 2)) / width;

                // Fortschritt der Welle (kombiniert Distanz und Noise)
                double localP = Math.clamp((p - distToCenter * 0.5 - noise * 0.1) / 0.4, 0, 1);

                if (localP <= 0) {
                    // Statisches Bild (kein Antialiasing gegen Haarlinien)
                    target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    target.drawImage(source, x, y, x + gridSize + 1, y + gridSize + 1, x, y, x + gridSize, y + gridSize, null);
                } else if (localP < 0.98) {
                    target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    target.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    // 3. Wasser-Bewegung:
                    // Zellen "wobbeln" horizontal und vertikal basierend auf Noise
                    double waveOffset = Math.sin(p * Math.PI * 4 + noise * 10) * 10 * localP;
                    double scale = 1.0 - Math.pow(localP, 2); // Beschleunigtes Schrumpfen

                    AffineTransform tx = new AffineTransform(canvasTransform);
                    // Verschiebung durch die "Welle" + Zentrierung
                    tx.translate(x + gridSize / 2.0 + waveOffset, y + gridSize / 2.0 + waveOffset);
                    tx.scale(scale, scale);
                    tx.rotate(noise * localP); // Leichte Drehung im Wasser
                    tx.translate(-gridSize / 2.0, -gridSize / 2.0);

                    target.setTransform(tx);

                    // Zeichnen der Zelle
                    target.drawImage(source,
                        0, 0, gridSize, gridSize,
                        x, y, x + gridSize, y + gridSize,
                        null);


                    target.setTransform(canvasTransform);
                }
            }
        }
        target.setTransform(canvasTransform);
    }
}
