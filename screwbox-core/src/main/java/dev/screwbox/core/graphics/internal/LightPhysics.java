package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Borders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparingDouble;
import static java.util.Objects.requireNonNull;

public class LightPhysics {

    private static class Occluder {
        private final Bounds bounds;
        private final boolean isSelfOcclude;
        private List<Line> lines;

        Occluder(final Bounds bounds, final boolean isSelfOcclude) {
            requireNonNull(bounds, "occluder must not be null");
            this.bounds = bounds;
            this.isSelfOcclude = isSelfOcclude;
        }

        public List<Line> lines(Vector lightPosition) {
            if(lines == null) {
                if(isSelfOcclude) {
                    lines = Borders.ALL.extractFrom(bounds);

                } else {
                    List<Line> lightDependendLines = new ArrayList<>();
                    final boolean isBetweenX = lightPosition.x() > bounds.minX() && lightPosition.x() < bounds.maxX();
                    final boolean isBetweenY = lightPosition.y() > bounds.minY() && lightPosition.y() < bounds.maxY();
                    final List<Line> borders = new ArrayList<>(Borders.ALL.extractFrom(bounds));//TODO can also be cached
                    borders.sort(comparingDouble(border -> border.center().distanceTo(lightPosition)));
                    if (isBetweenX != isBetweenY) {
                        lightDependendLines.add(borders.get(borders.get(1).intersects(Line.between(bounds.position(), lightPosition)) ? 0 : 1));
                    }
                    lightDependendLines.add(borders.get(2));
                    lightDependendLines.add(borders.get(3));
                    return lightDependendLines;
                }
            }
            return lines;
        }

        public Optional<Vector> nearestHit(Line probe) {
            return probe.closestIntersectionToStart(lines(probe.start()));
        }
    }

    private final List<Occluder> occluders = new ArrayList<>();

    public void addOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        occluders.add(new Occluder(occluder, true));
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        occluders.add(new Occluder(occluder, false));
    }

    public boolean isOccluded(final Line source) {
        var box = new DirectionalLightBox(source, 1);
        for (final var occluder : occluders) {
            if (box.intersects(occluder.bounds)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOccluded(final Vector position) {
        for (final var occluder : occluders) {
            if (occluder.bounds.contains(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector> calculateArea(final Bounds lightBox, double minAngle, double maxAngle) {
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Vector> area = new ArrayList<>();
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        final var relevantOccluders = allIntersecting(lightBox);
        for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
            final Line raycast = Angle.degrees(angle).rotate(normal);
            double minDist = Double.MAX_VALUE;
            Vector nearest = null;
            for(var occluder : relevantOccluders) {
                var hit = occluder.nearestHit(raycast);
                if (hit.isPresent()) {
                    var dist = hit.get().distanceTo(raycast.start());
                    if(dist < minDist) {
                        minDist = dist;
                        nearest = hit.get();
                    }
                }
            }
            area.add(nearest == null ? raycast.end() : nearest);
        }
        return area;
    }

    private List<Occluder> allIntersecting(Bounds box) {
        List<Occluder> intersecting = new ArrayList<>();
        for (final var occluder : occluders) {
            if (occluder.bounds.intersects(box)) {
                intersecting.add(occluder);
            }
        }
        return intersecting;
    }

    public List<Vector> calculateArea(final DirectionalLightBox lightBox) {
        final List<Vector> poi = new ArrayList<>();
        List<Occluder> relevantOccluders = new ArrayList<>();
        for (final var occluder : occluders) {
            if (lightBox.intersects(occluder.bounds)) {
                relevantOccluders.add(occluder);
                poi.add(occluder.bounds.origin());
                poi.add(occluder.bounds.topRight());
                poi.add(occluder.bounds.bottomRight());
                poi.add(occluder.bounds.bottomLeft());
            }
        }

        List<Line> lightProbes = calculateLightProbes(lightBox, poi);

        final List<Line> definitionLines = new ArrayList<>();
        for (final var probe : lightProbes) {
            List<Line> combined = new ArrayList<>();
            Line nextByOccluder = findClosest(probe, relevantOccluders).map(closest -> Line.between(probe.start(), closest)).orElse(probe);
            Line nextByNonSelfOccluderOccluder = probe.closestIntersectionToStart(combined).map(closest -> Line.between(probe.start(), closest)).orElse(probe);
            definitionLines.add(nextByOccluder.length() < nextByNonSelfOccluderOccluder.length() ? nextByOccluder : nextByNonSelfOccluderOccluder);
        }
        definitionLines.sort(comparingDouble(o -> o.start().distanceTo(lightBox.origin())));
        final List<Vector> area = new ArrayList<>(definitionLines.size() + 2);
        for (final var line : definitionLines) {
            area.add(line.end());
        }
        area.add(lightBox.topRight());
        area.add(lightBox.origin());
        return area;
    }

    private static Optional<Vector> findClosest(Line probe, List<Occluder> relevantOccluders) {
        double minDist = Double.MAX_VALUE;
        Vector closest = null;
        for (final var occluder : relevantOccluders) {
            var hit = occluder.nearestHit(probe);
            if(hit.isPresent()) {
                var dist = hit.get().distanceTo(probe.start());
                if(dist < minDist) {
                    minDist = dist;
                    closest = hit.get();
                }
            }
        }
        return Optional.ofNullable(closest);
    }

    private static List<Line> calculateLightProbes(DirectionalLightBox lightBox, List<Vector> poi) {
        List<Line> lightProbes = new ArrayList<>();
        var a = lightBox.source().end().substract(lightBox.source().start()).length(0.000000000001);
        var b = lightBox.source().start().substract(lightBox.source().end()).length(0.000000000001);

        for (final var p : poi) {
            lightBox.source().perpendicular(p).ifPresent(perpendicular -> {
                    lightProbes.add(perpendicular.move(a).length(lightBox.distance()));
                    lightProbes.add(perpendicular);
                    lightProbes.add(perpendicular.move(b).length(lightBox.distance()));
                }
            );
        }

        lightProbes.add(Line.between(lightBox.origin(), lightBox.bottomLeft()));
        lightProbes.add(Line.between(lightBox.topRight(), lightBox.bottomRight()));
        return lightProbes;
    }
}
