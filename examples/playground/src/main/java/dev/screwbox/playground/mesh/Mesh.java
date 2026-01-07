package dev.screwbox.playground.mesh;

import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static java.util.Objects.nonNull;

public class Mesh<T> { //TODO implement Graph?

    private final List<Connection<T>> connections = new ArrayList<>();

    private Map<T, List<T>> byStart = new HashMap<>();
    private Set<T> ends = new HashSet<>();

    private record Connection<T>(T start, T end) {
    }

    public void addConnection(final T start, final T end) {
        Validate.isTrue(() -> isConnectedToMesh(start), "start node is not connected to mesh: " + start);
        Validate.isFalse(() -> areNodesConnected(start, end), "connection already exists: " + start + " -> " + end);
        connections.add(new Connection<T>(start, end));
        byStart.computeIfAbsent(start, value -> new ArrayList<>()).add(end);
        ends.add(end);
    }

    public boolean isConnectedToMesh(final T node) {
        return !(!byStart.isEmpty() && !hasEndNode(node) && !hasStartNode(node));
    }

    public boolean areNodesConnected(final T start, final T end) {
        var startConnections = byStart.get(start);
        if (nonNull(startConnections) && startConnections.contains(end)) {
            return true;
        }
        var endConnections = byStart.get(end);
        return nonNull(endConnections) && endConnections.contains(start);
    }

    private boolean hasEndNode(T end) {
        return ends.contains(end);
    }

    public boolean hasStartNode(final T node) {
        return byStart.containsKey(node);
    }

    public List<List<T>> fetchAtomicCycles(final T start) {
        if(!hasStartNode(start)) {
            return Collections.emptyList();
        }
        Queue<T> unprocessed = new LinkedList<>(byStart.get(start));

        return new ArrayList<>();
    }
    //TODO .triangles()
    //TODO .detailedTriangles(3)
}
