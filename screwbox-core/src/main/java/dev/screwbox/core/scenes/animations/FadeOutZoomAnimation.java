package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Offset;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public record FadeOutZoomAnimation(Offset center) implements TransitionAnimation {


    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final double progress = context.progress().value();

        // 1. Hintergrundbild zeichnen
        target.drawImage(source, 0, 0, context.width(), context.height(), null);

        // Bestimme das Zentrum (Konstruktor-Param oder Bildschirmmitte)
        final double centerX = center == null ? context.width() / 2.0 : center.x() * context.resolutionScale();
        final double centerY = center == null ? context.height() / 2.0 : center.y() * context.resolutionScale();

        // Berechne maximale Distanz zu einer der Ecken, um den Start-Radius zu bestimmen
        final double maxDistX = Math.max(centerX, context.width() - centerX);
        final double maxDistY = Math.max(centerY, context.height() - centerY);
        final double maxRadius = Math.sqrt(maxDistX * maxDistX + maxDistY * maxDistY);

        final double currentRadius = maxRadius * (1.0 - progress);

        // 2. Schwarze Maske (außerhalb des Kreises)
        final Area mask = new Area(new Rectangle(0, 0, context.width(), context.height()));
        if (currentRadius > 0) {
            final Ellipse2D.Double iris = new Ellipse2D.Double(
                centerX - currentRadius,
                centerY - currentRadius,
                currentRadius * 2,
                currentRadius * 2);
            mask.subtract(new Area(iris));
        }

        target.setColor(Color.BLACK);
        target.fill(mask);

        // 3. Weicher Rand (Vignette)
        if (currentRadius > 10) {
            final Point2D gradientCenter = new Point2D.Double(centerX, centerY);
            final float[] dist = { 0.8f, 1.0f };
            final Color[] colors = { new Color(0, 0, 0, 0), Color.BLACK };

            final RadialGradientPaint gradient = new RadialGradientPaint(
                gradientCenter,
                (float) (currentRadius + 5),
                dist,
                colors);

            target.setPaint(gradient);
            target.fill(new Ellipse2D.Double(
                centerX - currentRadius - 5,
                centerY - currentRadius - 5,
                (currentRadius + 5) * 2,
                (currentRadius + 5) * 2));
        }
    }
}