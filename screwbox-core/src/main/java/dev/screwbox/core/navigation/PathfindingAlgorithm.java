package dev.screwbox.core.navigation;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.internal.ChainedNode;
import org.w3c.dom.Node;

import java.util.List;

public interface PathfindingAlgorithm<T> {

    List<T> findPath(NodeGraph<T> nodeGraph, T start, T end);

}
