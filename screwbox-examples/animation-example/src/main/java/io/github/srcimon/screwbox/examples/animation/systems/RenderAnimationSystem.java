package io.github.srcimon.screwbox.examples.animation.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;

import java.util.*;
import java.util.stream.Collectors;

public class RenderAnimationSystem implements EntitySystem {

    private static final Archetype DOTS = Archetype.of(TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        List<Vector> dotPositions = new ArrayList<>(engine.entities().fetchAll(DOTS).stream()
                .map(dot -> dot.get(TransformComponent.class).bounds.position())
                .toList());



        for(final var position : dotPositions) {
            engine.graphics().world().fillCircle(position, 8, Color.WHITE);
        }

        while (!dotPositions.isEmpty()) {
            var start = dotPositions.get(0);
            var allThree =  findNearestTwo(start, dotPositions);
            if(allThree.isEmpty()) {
                dotPositions.clear();
            } else {
                engine.graphics().world().drawLine(start, allThree.get(1), Color.RED);
                engine.graphics().world().drawLine(start, allThree.get(2), Color.RED);
                dotPositions.removeAll(allThree);
            }
        }
    }

    private List<Vector> findNearestTwo(Vector start, List<Vector> positions) {
        if(positions.size() < 3) {
            return Collections.emptyList();
        }
        List<Vector> byDistance = new ArrayList<>();
        byDistance.addAll(positions);

        byDistance.sort(new Comparator<Vector>() {
            @Override
            public int compare(Vector me, Vector other) {
                return Double.compare(me.distanceTo(start), other.distanceTo(start));
            }
        });

        return List.of(byDistance.get(0), byDistance.get(1), byDistance.get(2));
    }

}
