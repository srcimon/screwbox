package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.PerlinNoise;

import java.util.ArrayList;
import java.util.List;

//TODO document and test
//TODO document in docusaurus
public record LensFlare(List<Reflection> reflections, int rayCount) {

    //TODO LensFlareBundle
    private LensFlare() {
        this(new ArrayList<>(), 0);
    }

    private record Reflection(double distance, double size, double opacity) {

    }

    public static LensFlare noRays() {
        return new LensFlare();
    }

    public static LensFlare rays(int rayCount) {
        return new LensFlare(new ArrayList<>(), rayCount);
    }


    public LensFlare addReflection(final double distance, final double size, final double opacity) {
        ArrayList<Reflection> irises1 = new ArrayList<>(reflections);
        irises1.add(new Reflection(distance, size, opacity));
        return new LensFlare(irises1, rayCount);
    }

    public void render(final Vector position, final double radius, final Color color, final Viewport viewport) {
        renderRays(position, radius, color, viewport);
        renderReflections(radius, color, viewport, position);
        renderOrbs(position, radius, color, viewport);
    }

    private void renderReflections(double radius, Color color, Viewport viewport, Vector position) {
        final Vector cameraPosition = viewport.camera().position();
        final var positionToCamera = cameraPosition.substract(position);

        for (final var orb : reflections) {
            final var orbPosition = cameraPosition.add(positionToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbOptions = CircleDrawOptions.fading(color.opacity(color.opacity().value() * orb.opacity()));
            viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
        }
    }

    private  void renderRays(Vector position, double radius, Color color, Viewport viewport) {
        double rayRotationSpeed = 0.1;
        double rayOpacity = 0.14;
        int rayWidth = 1;
        double rayLength = 2.0;

        for (int i = 0; i < rayCount; i++) {
            var line = Line.normal(position, rayLength * radius);
            var result = Rotation.degrees(i * 360.0 / rayCount + rayRotationSpeed * viewport.toCanvas(position).x()).applyOn(line);
            LineDrawOptions options = LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidth);
            viewport.canvas().drawLine(viewport.toCanvas(result.from()), viewport.toCanvas(result.to()), options);
        }
    }

    private  void renderOrbs(Vector position, double radius, Color color, Viewport viewport) {

        int distance = 60;
        int spacing = 30;
        double zoom = 1000;
        double opacity = 0.2;
        double size = 0.1;
        int count = 8;

        // orbs
        for (double d = 0; d < 360; d += 360.0 / count) {
            var to = Rotation.degrees(d).applyOn(Line.normal(position, spacing)).to();
            Offset offset = viewport.toCanvas(to);
            double z = radius / 10.0 + color.rgb() / 10.0;
            double z2 = radius / 3.0 + color.rgb() / 8.0;

            var noise = Percent.of((PerlinNoise.generatePerlinNoise3d((int) d + 1938891L, offset.x() / zoom, offset.y() / zoom, z) + 1) / 2.0);
            var noise2 = Percent.of((PerlinNoise.generatePerlinNoise3d((int) d + 1485952L, offset.x() / zoom, offset.y() / zoom, z2) + 1) / 2.0);
            Offset dotOffset = viewport.toCanvas(to.add(noise.rangeValue(-distance, distance), noise2.rangeValue(-distance, distance)));

            viewport.canvas().drawCircle(dotOffset, (int) (viewport.toCanvas(radius) * size), CircleDrawOptions.filled(color.opacity(color.opacity().value() * opacity * noise.value())));
        }
    }
}
