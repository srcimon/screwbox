package dev.screwbox.playground.mesh;

import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mesh<T> { //TODO implement Graph?

    private final List<Connection<T>> connections = new ArrayList<>();

    private Map<T, List<T>> byStart = new HashMap<>();
    private Set<T> ends = new HashSet<>();

    private record Connection<T>(T start, T end) {
    }

    static int inn = 0;
    public void addConnection(final T start, final T end) {
        if(!byStart.isEmpty() && !hasEndNode(start) && !hasStartNode(start)) {
            throw new IllegalArgumentException("start node is not connected to mesh: " + start);
        }
        if(isConnected(start, end)) {
            throw new IllegalArgumentException("connection already exists: " + start + " -> " + end);
        }
        connections.add(new Connection<T>(start, end));
        byStart.computeIfAbsent(start, value -> new ArrayList<>()).add(end);
        ends.add(end);
    }

    private boolean isConnected(T start, T end) {
        var startConnections = byStart.get(start);
        if(startConnections != null && startConnections.contains(end)) {
            return true;
        }
        var endConnections = byStart.get(end);
        if(endConnections != null && endConnections.contains(end)) {
            return true;
        }
        return false;
    }

    private boolean hasEndNode(T end) {
        return ends.contains(end);
    }

    public boolean hasStartNode(final T node) {
        return byStart.containsKey(node);
    }

    public int connectionCount() {
        return connections.size();
    }

    public List<List<T>> fetchAtomicCycles(final T start) {
        return new ArrayList<>();
    }
    //TODO .triangles()
    //TODO .detailedTriangles(3)
}
