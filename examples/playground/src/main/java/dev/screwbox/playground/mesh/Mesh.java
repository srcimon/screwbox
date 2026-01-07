package dev.screwbox.playground.mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mesh<T> { //TODO implement Graph?

    private final List<Connection<T>> connections = new ArrayList<>();

    private Map<T, List<T>> connectionMap = new HashMap<>();

    public boolean hasStartNode(T node) {
        return connectionMap.containsKey(node);
    }

    private record Connection<T>(T start, T end) {
    }

    public void addConnection(T start, T end) {
        connections.add(new Connection<T>(start, end));
        connectionMap.computeIfAbsent(start, value -> new ArrayList<>()).add(end);
    }

    public int connectionCount() {
        return connections.size();
    }


    //TODO .triangles()
    //TODO .detailedTriangles(3)
}
