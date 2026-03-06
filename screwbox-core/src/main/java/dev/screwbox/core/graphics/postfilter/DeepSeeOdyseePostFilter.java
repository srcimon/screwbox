package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.util.Random;

//TODO support split screen
public class DeepSeeOdyseePostFilter implements PostProcessingFilter {

    private float[] bubbleX, bubbleY, bubbleSpeed, bubbleSize;


    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        int w = source.getWidth(null);
        int h = source.getHeight(null);
        long time = context.lifetime().milliseconds();
        double zTime = time / 2000.0;

//TODO fix bubble motion (delta)

        // 2. UNDERWATER WOBBLE (Perlin-Distortion)
        // Wir nutzen das Gitter-System vom Fischauge, aber biegen es organisch
        target.drawImage(source, 0, 0, w, h, null);


        // 4. DEEP SEA LIGHTING (Caustics & Fog)
        float[] d = {0f, 0.6f, 1f};
        Color[] c = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};
        RadialGradientPaint deepBlue = new RadialGradientPaint(w / 2f, h / 2f, (float) (w * 0.8), d, c);
        target.setPaint(deepBlue);
        target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        target.fillRect(0, 0, w, h);

        if (bubbleX == null) {
            int bCount = 15; // Mehr Blasen für bessere Atmo
            bubbleX = new float[bCount];
            bubbleY = new float[bCount];
            bubbleSpeed = new float[bCount];
            bubbleSize = new float[bCount];
            Random r = new Random();
            for (int i = 0; i < bCount; i++) {
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
            bubbleX[i] += (float) (horizontalDrift * 2.0 + Math.sin(zTime + i) * 0.5);

            // Screen Wrap (unten neu spawnen, wenn oben aus dem Bild)
            if (bubbleY[i] < -20) {
                bubbleY[i] = h + 20;
                bubbleX[i] = new Random().nextInt(w);
            }
            // Horizontales Wrapping
            bubbleX[i] = (bubbleX[i] + w) % w;

            // Zeichnen mit Tiefenwirkung (kleinere Blasen sind blasser)
            int alpha = (int) (50 + (bubbleSize[i] * 20));
            target.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

            // Die Blase leicht deformieren (etwas oval beim Aufstieg)
            int bw = (int) bubbleSize[i];
            int bh = (int) (bubbleSize[i] * 1.1); // Leicht in die Länge gezogen

            target.fillOval((int) bubbleX[i], (int) bubbleY[i], bw, bh);

            // Optional: Ein kleiner Glanzpunkt
            target.setColor(new Color(255, 255, 255, 200));
            target.fillOval((int) bubbleX[i] + 1, (int) bubbleY[i] + 1, bw / 3, bh / 3);
        }
    }

}
