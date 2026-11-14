package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Graph;

import java.util.List;

//TODO may be a new feature
public class WaypointGraph implements Graph<Vector> {

    @Override
    public List<Vector> adjacentNodes(Vector node) {
        return List.of();
    }

    @Override
    public double traversalCost(Vector start, Vector end) {
        return 0;
    }

    @Override
    public Vector toGraph(Vector position) {
        return null;
    }

    @Override
    public Vector toPosition(Vector node) {
        return null;
    }

    @Override
    public boolean nodeExists(Vector node) {
        return false;
    }
}
