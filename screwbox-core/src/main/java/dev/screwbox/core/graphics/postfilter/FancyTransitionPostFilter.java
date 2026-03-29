package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.shader.GreyscaleShader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RGBImageFilter;
import java.awt.image.FilteredImageSource;

/**
 * Ein hocheffektiver Übergangsfilter, der das Bild in Fragmente zerlegt und entfärbt.
 */
public record FancyTransitionPostFilter(Percent progress) implements PostProcessingFilter {

    private static final int RINGS = 10;

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final double p = progress.value();

        // 1. Hard-Exit für 0% Progress -> Zeichne einfach das Originalbild
        if (p <= 0) {
            target.drawImage(source, area.x(), area.y(), area.width(), area.height(), null);
            return;
        }

        final int cx = area.center().x();
        final int cy = area.center().y();

        // Berechne den maximalen Radius, um die Ecken des Bildschirms abzudecken
        final double maxRadius = Math.sqrt(Math.pow(area.width(), 2) + Math.pow(area.height(), 2)) / 2.0;
        final double ringWidth = maxRadius / RINGS;

        for (int i = 0; i < RINGS; i++) {
            final double ringFactor = (double) i / RINGS;
            // Individueller Fortschritt pro Ring (äußere Ringe starten verzögert)
            final double localP = Math.max(0, Math.min(1, (p - ringFactor * 0.4) / 0.6));

            final double rStart = i * ringWidth;
            final double rEnd = rStart + ringWidth + 0; // +1 gegen Pixellücken

            // Ring-Geometrie erstellen
            final Area ring = new Area(new java.awt.geom.Ellipse2D.Double(cx - rEnd, cy - rEnd, rEnd * 2, rEnd * 2));
            ring.subtract(new Area(new java.awt.geom.Ellipse2D.Double(cx - rStart, cy - rStart, rStart * 2, rStart * 2)));

            // WICHTIG: Den Ring auf den sichtbaren Bereich zuschneiden
            ring.intersect(new Area(new Rectangle2D.Double(area.x(), area.y(), area.width(), area.height())));

            final var oldClip = target.getClip();
            target.setClip(ring);

            // Transformationen
            final AffineTransform tx = new AffineTransform();
            tx.translate(cx, cy);
            tx.rotate(localP * Math.PI * 0.8 * (1 + ringFactor));
            tx.scale(1.0 - (localP * 0.5), 1.0 - (localP * 0.5));
            tx.translate(-cx, -cy);

            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localP)));
            target.drawImage(source, tx, null);

            target.setClip(oldClip);
        }
    }
}