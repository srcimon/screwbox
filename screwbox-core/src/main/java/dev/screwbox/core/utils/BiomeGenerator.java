package dev.screwbox.core.utils;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.AwtMapper;

import java.awt.image.BufferedImage;

//TODO Document
//TODO Test
//TODO changelog
//TODO rename?
public class BiomeGenerator {

    private final long seed;
    private final double zoom;

    public BiomeGenerator(long seed, double zoom) {
        this.seed = seed;
        this.zoom = zoom;
    }

    public static void main(String[] args) {
        new BiomeGenerator(12301, 60).createPreview(Size.of(400,300)).exportPng("preview.png");
    }

    public static Percent noise(double zoom, long seed, Offset offset) {
        double value = 0.0;
        for (double gridZoom = zoom; gridZoom >= 1; gridZoom /= 2.0) {
            value += PerlinNoise.generatePerlinNoise(seed, offset.x() / gridZoom, offset.y() / gridZoom) * gridZoom;
        }
        return Percent.of((value / zoom + 1) / 2.0);
    }

    public Frame createPreview(Size size) {
        BufferedImage bb = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
        var gr = bb.getGraphics();
        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                //TODO use filter here
                var per = noise(zoom, seed, Offset.at(x, y));
                gr.setColor(AwtMapper.toAwtColor(Color.rgb(per.rangeValue(0, 255),0,0)));
                gr.drawRect(x, y, 1, 1);
            }
        }
        gr.dispose();
        return Frame.fromImage(bb);
    }
}
