package dev.screwbox.playground.mesh;

import java.util.ArrayList;
import java.util.List;

public class Mesh<T> {

    private final List<Connection<T>> connections = new ArrayList<>();

    private record Connection<T>(T start, T end) {
    }

    public void addConnection(T start, T end) {
        connections.add(new Connection<T>(start, end));
    }

    public int connectionCount() {
        return connections.size();
    }


    //TODO .triangles()
    //TODO .detailedTriangles(3)
}
