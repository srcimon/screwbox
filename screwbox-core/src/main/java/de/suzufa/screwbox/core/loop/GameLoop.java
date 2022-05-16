package de.suzufa.screwbox.core.loop;

public interface GameLoop {

    Metrics metrics();

    GameLoop setTargetFps(int targetFps);

    int targetFps();
}
