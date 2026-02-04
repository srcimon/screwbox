package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Borders;

import java.util.ArrayList;
import java.util.List;

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


    }

    @Deprecated
    private final List<Bounds> legacyOccluders = new ArrayList<>();

    @Deprecated
    private final List<Bounds> legacyNoSelfOccluders = new ArrayList<>();

    private final List<Occluder> occluders = new ArrayList<>();

    public void addOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        legacyOccluders.add(occluder);
        occluders.add(new Occluder(occluder, true));
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        legacyNoSelfOccluders.add(occluder);
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
        final var relevantOccluders = allIntersecting(lightBox);
        final List<Line> occluderOutlines = extractLinesFromOccluders(relevantOccluders, lightBox.position());
        final List<Vector> area = new ArrayList<>();
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
            final Line raycast = Angle.degrees(angle).rotate(normal);
            area.add(raycast.closestIntersectionToStart(occluderOutlines).orElse(raycast.end()));
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

    private static void addFarDistanceLines(final List<Line> allLines, final List<Bounds> allBounds, final Vector position) {
        for (final var bounds : allBounds) {
            final boolean isBetweenX = position.x() > bounds.minX() && position.x() < bounds.maxX();
            final boolean isBetweenY = position.y() > bounds.minY() && position.y() < bounds.maxY();
            final List<Line> borders = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            borders.sort(comparingDouble(border -> border.center().distanceTo(position)));
            if (isBetweenX != isBetweenY) {
                allLines.add(borders.get(borders.get(1).intersects(Line.between(bounds.position(), position)) ? 0 : 1));
            }
            allLines.add(borders.get(2));
            allLines.add(borders.get(3));
        }
    }

    private static List<Line> extractLinesFromOccluders(final List<Occluder> myOccluders, Vector lightPosition) {
        final List<Line> allLines = new ArrayList<>();
        for (final var occluder : myOccluders) {
            allLines.addAll(occluder.lines(lightPosition));
        }
        return allLines;
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

        List<Bounds> relevantNonSelfOccluders = new ArrayList<>();
        for (final var occluder : legacyNoSelfOccluders) {
            if (lightBox.intersects(occluder)) {
                relevantNonSelfOccluders.add(occluder);
                poi.add(occluder.origin());
                poi.add(occluder.topRight());
                poi.add(occluder.bottomRight());
                poi.add(occluder.bottomLeft());
            }
        }


        List<Line> lightProbes = calculateLightProbes(lightBox, poi);

        final List<Line> definitionLines = new ArrayList<>();
        for (final var probe : lightProbes) {
            final List<Line> occluderOutlines = extractLinesFromOccluders(relevantOccluders, probe.start());
            List<Line> combined = new ArrayList<>();
            addFarDistanceLines(combined, relevantNonSelfOccluders, probe.start());
            Line nextByOccluder = probe.closestIntersectionToStart(occluderOutlines).map(closest -> Line.between(probe.start(), closest)).orElse(probe);
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
