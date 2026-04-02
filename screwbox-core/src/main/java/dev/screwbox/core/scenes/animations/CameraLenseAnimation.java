package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
//TODO document
//TODO refactor
//TODO test

/**
 * A camera lense zoom in transition.
 *
 * @since 3.26.0
 */
public class CameraLenseAnimation implements TransitionAnimation {

    private static final int RINGS = 14;

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        final double p = progress.value();

        final int cx = size.center().x();
        final int cy = size.center().y();
        final double maxR = Math.sqrt(Math.pow(size.width(), 2) + Math.pow(size.height(), 2)) / 2.0;
        final double step = maxR / RINGS;

        for (int i = 0; i < RINGS; i++) {
            // Stagger-Effekt: Äußere Ringe starten früher
            final double ringFactor = (double) i / RINGS;
            final double localP = Math.clamp((p - (1 - ringFactor) * 0.3) / 0.7, 0, 1);

            if (localP <= 0) {
                drawRing(source, target, cx, cy, i * step, (i + 1) * step + 1, new AffineTransform(), 1.0f);
                continue;
            }

            // 1. Physisches Abreißen (Skalierung und Rotation)
            final double zScale = 1.0 + Math.pow(localP, 3) * 12.0;
            final double rotation = localP * localP * Math.PI * 0.6;

            // 2. Fade-out Logik: Erst im letzten Viertel (0.75 bis 1.0)
            float alpha = 1.0f;
            if (localP > 0.75) {
                alpha = (float) (1.0 - (localP - 0.75) * 4.0); // Skaliert 0.25 Rest-Progress auf 1.0 bis 0.0 Alpha
            }

            final AffineTransform tx = new AffineTransform();
            tx.translate(cx, cy);
            tx.rotate(rotation);
            tx.scale(zScale, zScale);
            tx.translate(-cx, -cy);

            drawRing(source, target, cx, cy, i * step, (i + 1) * step + 1, tx, alpha);
        }
    }

    private void drawRing(Image src, Graphics2D g, int cx, int cy, double r1, double r2, AffineTransform tx, float alpha) {
        final Area ring = new Area(new Ellipse2D.Double(cx - r2, cy - r2, r2 * 2, r2 * 2));
        ring.subtract(new Area(new Ellipse2D.Double(cx - r1, cy - r1, r1 * 2, r1 * 2)));

        final Shape oldClip = g.getClip();
        final var oldComposite = g.getComposite();

        g.setClip(ring);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));
        g.drawImage(src, tx, null);

        g.setComposite(oldComposite);
        g.setClip(oldClip);
    }
}