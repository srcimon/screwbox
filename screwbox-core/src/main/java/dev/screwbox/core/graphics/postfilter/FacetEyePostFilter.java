package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Distorts the image seen through a facet eye.
 *
 * @since 3.24.0
 */
public record FacetEyePostFilter(int eyeSize) implements PostProcessingFilter {

    public FacetEyePostFilter {
        Validate.range(eyeSize, 8, 128, "eye size must be in range 8 to 128");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        drawSourceImage(source, target, context);
        final var area = context.bounds();

        final int scaledEyeSize = (int) Math.max(1, eyeSize * context.resolutionScale());

        final double maxDist = Math.sqrt(Math.pow(context.width() / 2.0, 2) + Math.pow(context.height() / 2.0, 2));

        for (int y = area.y(); y < area.y() + context.height(); y += scaledEyeSize) {
            final int xOffset = ((y - area.y()) / scaledEyeSize % 2 == 0) ? 0 : scaledEyeSize / 2;

            for (int x = area.x() - xOffset; x < area.x() + context.width(); x += scaledEyeSize) {
                final double dx = (x + scaledEyeSize / 2.0) - area.center().x();
                final double dy = (y + scaledEyeSize / 2.0) - area.center().y();
                final double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;

                final double localZoom = 0.9 - (dist * 0.6);
                final int srcW = (int) (scaledEyeSize / localZoom);
                final int srcH = (int) (scaledEyeSize / localZoom);
                final int srcX = x - (srcW - scaledEyeSize) / 2;
                final int srcY = y - (srcH - scaledEyeSize) / 2;

                target.drawImage(source,
                    x, y, x + scaledEyeSize, y + scaledEyeSize,
                    srcX, srcY, srcX + srcW, srcY + srcH,
                    null);
            }
        }
    }
}
