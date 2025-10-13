package dev.screwbox.core.navigation;

import java.util.List;

public interface PathfindingAlgorithm<T> {

    List<T> findPath(Graph<T> graph, T start, T end);

}
