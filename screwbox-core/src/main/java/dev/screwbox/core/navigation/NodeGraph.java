package dev.screwbox.core.navigation;

import java.util.List;

public interface NodeGraph<T> {

    List<T> adjacentNodes(T node);

    double traversalCost(T start, T end);
}
