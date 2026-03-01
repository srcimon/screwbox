package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.Random;

//TODO support split screen
public class DeepSeeOdyseePostFilter implements PostProcessingFilter {

    private float[] bubbleX, bubbleY, bubbleSpeed, bubbleSize;


    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawDeepSeaOdyssey(target, source);
    }

    public void drawDeepSeaOdyssey(Graphics g, Image screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth(null);
        int h = screenBuffer.getHeight(null);
        long time = System.currentTimeMillis();
        double zTime = time / 2000.0;


        // 2. UNDERWATER WOBBLE (Perlin-Distortion)
        // Wir nutzen das Gitter-System vom Fischauge, aber biegen es organisch
        int gridSize = 32;
        double centerX = w / 2.0, centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {
                // Perlin Noise für die Strömung
                double noise = PerlinNoise.generatePerlinNoise3d(110L, x * 0.005, y * 0.005, zTime);

                // Fischauge-Faktor
                double dx = x - centerX, dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;
                double fischEye = 1.0 - 0.2 * (dist * dist);

                int tx = (int) (centerX + dx * fischEye + noise * 20);
                int ty = (int) (centerY + dy * fischEye + Math.sin(zTime + x*0.01) * 10);

                g2d.setComposite(AlphaComposite.SrcOver);
                g2d.drawImage(screenBuffer, tx, ty, tx + gridSize + 1, ty + gridSize + 1,
                    x, y, x + gridSize, y + gridSize, null);
            }
        }


        // 4. DEEP SEA LIGHTING (Caustics & Fog)
        float[] d = {0f, 0.6f, 1f};
        Color[] c = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};
        RadialGradientPaint deepBlue = new RadialGradientPaint(w/2f, h/2f, (float)(w*0.8), d, c);
        g2d.setPaint(deepBlue);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.fillRect(0, 0, w, h);

        if (bubbleX == null) {
            int bCount = 15; // Mehr Blasen für bessere Atmo
            bubbleX = new float[bCount]; bubbleY = new float[bCount];
            bubbleSpeed = new float[bCount]; bubbleSize = new float[bCount];
            Random r = new Random();
            for(int i=0; i<bCount; i++) {
                bubbleX[i] = r.nextInt(w);
                bubbleY[i] = r.nextInt(h);
                bubbleSpeed[i] = 1.0f + r.nextFloat() * 2.0f; // Individuelle Geschwindigkeit
                bubbleSize[i] = 2 + r.nextInt(5);             // Unterschiedliche Größen
            }
        }

// 5. REALISTISCHE LUFTBLASEN
        for (int i = 0; i < bubbleX.length; i++) {
            // Perlin Noise für das horizontale "Tandeln" (Wobble)
            // Wir nutzen x, y und die Zeit, damit jede Blase eigenständig auf die Strömung reagiert
            double horizontalDrift = PerlinNoise.generatePerlinNoise3d(220L, bubbleX[i] * 0.01, bubbleY[i] * 0.01, zTime);

            // Bewegung: Auftrieb + Sinus-Wobble + Perlin-Drift
            bubbleY[i] -= bubbleSpeed[i];
            bubbleX[i] += (float)(horizontalDrift * 2.0 + Math.sin(zTime + i) * 0.5);

            // Screen Wrap (unten neu spawnen, wenn oben aus dem Bild)
            if (bubbleY[i] < -20) {
                bubbleY[i] = h + 20;
                bubbleX[i] = new Random().nextInt(w);
            }
            // Horizontales Wrapping
            bubbleX[i] = (bubbleX[i] + w) % w;

            // Zeichnen mit Tiefenwirkung (kleinere Blasen sind blasser)
            int alpha = (int)(50 + (bubbleSize[i] * 20));
            g2d.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

            // Die Blase leicht deformieren (etwas oval beim Aufstieg)
            int bw = (int)bubbleSize[i];
            int bh = (int)(bubbleSize[i] * 1.1); // Leicht in die Länge gezogen

            g2d.fillOval((int)bubbleX[i], (int)bubbleY[i], bw, bh);

            // Optional: Ein kleiner Glanzpunkt
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval((int)bubbleX[i] + 1, (int)bubbleY[i] + 1, bw/3, bh/3);
        }
    }
}
