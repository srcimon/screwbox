package dev.screwbox.core.scenes.animations;

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
public record CameraLensAnimation(int ringCount) implements TransitionAnimation {

    /**
     * Create an instance with default ring count.
     */
    public CameraLensAnimation() {
        this(14);
    }

    public CameraLensAnimation {
        Validate.range(ringCount, 4, 20, "ring count must be in range 4 to 20");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int cx = context.center().x();
        final int cy = context.center().y();
        final double maxR = Math.sqrt(Math.pow(context.width(), 2) + Math.pow(context.height(), 2)) / 2.0;
        final double step = maxR / ringCount;

        for (int i = 0; i < ringCount; i++) {
            final double ringFactor = (double) i / ringCount;
            final double localP = Math.clamp((context.progress().value() - (1 - ringFactor) * 0.3) / 0.7, 0, 1);

            if (localP <= 0) {
                target.setClip(createRingArea(cx, i, step, cy));
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, 1.0f)));
                target.drawImage(source, new AffineTransform(), null);

            } else {
                final double zScale = 1.0 + Math.pow(localP, 3) * 12.0;
                final double rotation = localP * localP * Math.PI * 0.6;
                final float alpha = localP > 0.75
                    ? (float) (1.0 - (localP - 0.75) * 4.0)
                    : 1.0f;

                final var transform = new AffineTransform();
                transform.translate(cx, cy);
                transform.rotate(rotation);
                transform.scale(zScale, zScale);
                transform.translate(-cx, -cy);

                target.setClip(createRingArea(cx, i, step, cy));
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));
                target.drawImage(source, transform, null);
            }
        }
    }

    private static Area createRingArea(final int cx, final int i, final double step, final int cy) {
        final Area ring = new Area(new Ellipse2D.Double(cx - ((i + 1) * step + 1), cy - ((i + 1) * step + 1), ((i + 1) * step + 1) * 2, ((i + 1) * step + 1) * 2));
        ring.subtract(new Area(new Ellipse2D.Double(cx - i * step, cy - i * step, i * step * 2, i * step * 2)));
        return ring;
    }

}