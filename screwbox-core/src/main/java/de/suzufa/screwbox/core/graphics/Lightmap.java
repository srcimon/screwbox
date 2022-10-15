package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.graphics.internal.ImageUtil;

//TODO: javadoc and test
public class Lightmap {

    private final Dimension size;
    private static final java.awt.Color[] COLORS = new java.awt.Color[] {
            toAwtColor(Color.BLACK),
            toAwtColor(Color.TRANSPARENT) };

    public Lightmap(final Dimension size) {
        this.size = size;
    }

    private record PointLight(Offset position, int range, List<Offset> area) {

    }

    private final List<PointLight> pointLights = new ArrayList<>();

    public Lightmap addPointLight(final Offset position, final int range, final List<Offset> area) {
        pointLights.add(new PointLight(position, range, area));
        return this;
    }

    public Sprite createImage() {
        BufferedImage lightmapImage = new BufferedImage(size.width(), size.height(),
                BufferedImage.TYPE_INT_ARGB);

        // TODO: static?
        float[] fractions = new float[2];
        fractions[0] = 0.1f;
        fractions[1] = 1f;

        Graphics2D lightmapGraphics = (Graphics2D) lightmapImage.getGraphics();
        for (var pointLight : pointLights) {
            Polygon polygon = new Polygon();
            for (var node : pointLight.area) {
                polygon.addPoint(node.x(), node.y());
            }
            RadialGradientPaint paint = new RadialGradientPaint(
                    pointLight.position.x(), pointLight.position.y(),
                    pointLight.range,
                    fractions, COLORS);

            lightmapGraphics.setPaint(paint);
            lightmapGraphics.fillPolygon(polygon);
        }

        Image result = ImageUtil.applyFilter(lightmapImage, new InvertAlphaFilter());
        return Sprite.fromImage(result);
    }
}
