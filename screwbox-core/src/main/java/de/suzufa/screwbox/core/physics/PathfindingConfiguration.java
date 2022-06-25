package de.suzufa.screwbox.core.physics;

public class PathfindingConfiguration {

    PathfindingAlgorithm algorithm = new DijkstraAlgorithm();

    public PathfindingAlgorithm algorithm() {
        return algorithm;
    }

    public void setAlgorithm(PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

}
