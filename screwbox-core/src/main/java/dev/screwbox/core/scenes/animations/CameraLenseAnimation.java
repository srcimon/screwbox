package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
//TODO refactor
//TODO test

/**
 * A camera lense zoom in transition.
 *
 * @since 3.26.0
 */
//TODO apply resoultionscale
public record CameraLenseAnimation(int ringCount) implements TransitionAnimation {

    /**
     * Create an instance with default ring count.
     */
    public CameraLenseAnimation() {
        this(14);
    }

    public CameraLenseAnimation {
        Validate.range(ringCount, 4, 20, "ring count must be in range 4 to 20");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {

        final int cx = size.center().x();
        final int cy = size.center().y();
        final double maxR = Math.sqrt(Math.pow(size.width(), 2) + Math.pow(size.height(), 2)) / 2.0;
        final double step = maxR / ringCount;

        for (int i = 0; i < ringCount; i++) {
            // Stagger-Effekt: Äußere Ringe starten früher
            final double ringFactor = (double) i / ringCount;
            final double localP = Math.clamp((progress.value() - (1 - ringFactor) * 0.3) / 0.7, 0, 1);

            if (localP <= 0) {
                final Area ring = new Area(new Ellipse2D.Double(cx - ((i + 1) * step + 1), cy - ((i + 1) * step + 1), ((i + 1) * step + 1) * 2, ((i + 1) * step + 1) * 2));
                ring.subtract(new Area(new Ellipse2D.Double(cx - i * step, cy - i * step, i * step * 2, i * step * 2)));

                final Shape oldClip = target.getClip();
                final var oldComposite = target.getComposite();

                target.setClip(ring);
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, 1.0f)));
                target.drawImage(source, new AffineTransform(), null);

                target.setComposite(oldComposite);
                target.setClip(oldClip);
                continue;
            } else {

                // 1. Physisches Abreißen (Skalierung und Rotation)
                final double zScale = 1.0 + Math.pow(localP, 3) * 12.0;
                final double rotation = localP * localP * Math.PI * 0.6;

                // 2. Fade-out Logik: Erst im letzten Viertel (0.75 bis 1.0)
                float alpha = localP > 0.75
                    ? (float) (1.0 - (localP - 0.75) * 4.0)
                    : 1.0f;

                final AffineTransform tx = new AffineTransform();
                tx.translate(cx, cy);
                tx.rotate(rotation);
                tx.scale(zScale, zScale);
                tx.translate(-cx, -cy);

                final Area ring = new Area(new Ellipse2D.Double(cx - ((i + 1) * step + 1), cy - ((i + 1) * step + 1), ((i + 1) * step + 1) * 2, ((i + 1) * step + 1) * 2));
                ring.subtract(new Area(new Ellipse2D.Double(cx - i * step, cy - i * step, i * step * 2, i * step * 2)));
                target.setClip(ring);
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));
                target.drawImage(source, tx, null);
            }
        }
    }

}